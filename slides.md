---
marp: true
theme: istanbul
_class: title
_paginate: false
_color: white
_backgroundImage: url('ista-title-bg.png')
paginate: true
backgroundImage: url('ista-bg.png')
transition: in-out
math: mathjax
---

# Exploring Property-Based Testing <br>with JUnit-QuickCheck

Thomas Mahler, Senior IT-Architect, ista SE

---

# Recap: Unit Tests

- **Verification of Small Units**: Unit tests validate individual units or components in isolation, ensuring they function correctly.<br>*Typically by working with hand-crafted example data sets*. ➔ **Example-Based Testing**
- **Rapid Feedback Loop**: They provide quick feedback during development, aiding in early bug detection and easier debugging.
- **Test Driving Development**: They can drive development by providing a specification of the expected behavior and functionality.
- **Maintainability and Refactoring**: They serve as documentation and aid in refactoring by ensuring existing functionality remains intact.

--- 

# Limitations of Example-Based Testing

- **Limited Coverage**: Example-based tests cover specific scenarios, often leaving out edge cases and unanticipated inputs.
- **Maintenance Overhead**: Adding new examples for every edge case becomes cumbersome and may not ensure complete coverage.
- **Sensitive with Changes**: Tests can become fragile and require frequent updating when the codebase evolves.

### Difficult Scenarios:

- **Complex or Large Input Space**: Example-based tests struggle with complex inputs or scenarios with a large input space.
- **Undefined or Unpredictable Behavior**: They may not catch unexpected behavior or violations of *general rules*.


---

# Property-Based Testing with JUnit-QuickCheck

```java
@RunWith(JUnitQuickcheck.class)
public class StringPropertiesTest {

  @Property(trials = 1000)
  public void concatLength(String a, String b) {
    assertEquals(a.length() + b.length(), (a + b).length());
  }
}
```

### This snippet already tells a lot:

- JUnit-QuickCheck is a JUnit extension which comes with its own test runner (`JUnitQuickcheck.class`)
- The `@Property` annotation marks a test method as a property-based test
- The test instances to be generated are specified by the method parameters (here: `String a, String b`)
- We use JUnit-Assertions to verify the properties in terms of the generated test instances (here `a` and `b`)
- We can use the `trials` parameter to specify the number of test cases to generate (default is 100)

---
# Property-Based Testing is about...

- **... Testing general Properties**: 
  - Property-based testing focuses on specifying *general properties* that the code should *always satisfy*, irrespective of specific examples.
  - Tests are built around properties (aka. *invariants*) like *commutativity*, *associativity*, or other logical rules that should hold regardless of input specifics.
- **... Generating random Inputs**: It employs *generative testing*, automatically generating a large number of test cases based on defined properties.

### But it's not Random Testing / Fuzzing

- **Targeted vs. Random Testing**: Fuzz testing (aka Fuzzing) involves providing random inputs, often without a specific property to validate, whereas property-based testing focuses on specific properties.
- **Purpose and Scope**: Fuzz testing aims to discover vulnerabilities and crashes, while property-based testing aims to verify specified properties.


---

# A first practical example: Reversing a List

<div class="columns-2">
  <div>

```java
// non-destructive reversal of a list
private <T> List<T> reverse(List<T> original) {
  List<T> clone = new ArrayList<>(original);
  Collections.reverse(clone);
  return clone;
}


// traditional example-based test
@Test
public void reverseList() {
  // given
  List<Integer> aList = Arrays.asList(1, 2, 3);
  // when
  var bList = reverse(aList);
  // then
  assertEquals(bList, Arrays.asList(3, 2, 1));
}
```

  </div>

```java
@Property
public void reverseFirstBecomesLast(List<Integer> aList) {
  // when
  var bList = reverse(aList);
  // then
  assertEquals(getFirst(aList), getLast(bList));
}

@Property
public void reverseKeepsElements(List<Integer> aList) {
  // when
  var bList = reverse(aList);
  // then
  assertEquals(aList.stream().sorted().toList(), 
                bList.stream().sorted().toList());
}

@Property
public void reverseIsItsOwnInverse(List<Integer> aList) {
  // when
  var bList = reverse(reverse(aList));
  // then
  assertEquals(aList, bList);
}
```

  <div>

  </div>
</div>


---

# A Case Study: Employee Wellness Program (1/2)


<div class="columns-2">
  <div>

  ![](insuranceReduction.jpeg)

  </div>
  <div>


```java
public class InsuranceReduction {
  public static int computeReduction(Employee employee) {
    int totalReduction = 0;
    if (employee.alcoholUnitsPerWeek <= 20) {
      totalReduction += 30;
    }
    if (employee.filledInHealthAssessment) {
      totalReduction += 25;
    }
    if (employee.participateInHealthControl) {
      if (employee.bmi <= 27.5) {
        totalReduction += 50;
      } else if (employee.bmi <= 30) {
        totalReduction += 25;
      }
      if (!employee.isSmoker) {
        totalReduction += 50;
      } else if (employee.joinedStopSmokingClass) {
        totalReduction += 25;
      } else {
        totalReduction -= 75;
      }
    }
    return totalReduction;
  }

  public static class Employee {
    int alcoholUnitsPerWeek;
    boolean filledInHealthAssessment;
    boolean participateInHealthControl;
    double bmi;
    boolean isSmoker;
    boolean joinedStopSmokingClass;
  }
}
```

  </div>
</div>

---
# A Case Study: Employee Wellness Program (2/2)

<div class="columns-2">
  <div>

```java  
@Test
public void smokingButTakingClass() {
  // given
  Employee employee = new Employee();
  employee.alcoholUnitsPerWeek = 10;
  employee.filledInHealthAssessment = true;
  employee.participateInHealthControl = true;
  employee.bmi = 25;
  employee.isSmoker = true;
  employee.joinedStopSmokingClass = true;

  // when
  int reduction = computeReduction(employee);

  // then
  assertEquals(130, reduction);
}

@Test
public void smokingAndNotTakingClass() {
  // given
  Employee employee = new Employee();
  employee.alcoholUnitsPerWeek = 10;
  employee.filledInHealthAssessment = true;
  employee.participateInHealthControl = true;
  employee.bmi = 25;
  employee.isSmoker = true;
  employee.joinedStopSmokingClass = false;

  // when
  int reduction = computeReduction(employee);

  // then
  assertEquals(30, reduction);
}
```

  </div>
  <div>

```java
@Property
public void varyHealthAssessment(@From(Fields.class) Employee employee) {
  // when
  int reduction = computeReduction(employee);
  employee.filledInHealthAssessment = ! employee.filledInHealthAssessment;
  int reduction2 = computeReduction(employee);
  // then
  assertEquals(25, Math.abs(reduction-reduction2));
}

@Property
public void varyParticipateInClass(@From(Fields.class) Employee employee) {
  // given
  assumeTrue(employee.participateInHealthControl);
  assumeTrue(employee.isSmoker);

  // when
  int reduction = computeReduction(employee);
  employee.joinedStopSmokingClass = ! employee.joinedStopSmokingClass;
  int reduction2 = computeReduction(employee);

  // then
  assertEquals(100, Math.abs(reduction-reduction2));
}
```
  </div>
</div>


---

# JUnit-QuickCheck @ ista new submetering platform

<div class="columns-2">
<div>

```java
@Property(trials = 1000)
public void divideCurrencyValues_withGrossAndVat(BigDecimal v1, BigDecimal v2) 
  throws CalculationException {
    assumeThat(v2, not(equalTo(BigDecimal.ZERO)));

    // Given
    final CurrencyValueType euroCVT = new CurrencyValueType(Currency.getInstance(EUR));
    final CurrencyValue aValue = new CurrencyValue(v1, euroCVT);
    NumberValue bValue = new NumberValue(v2);
    aValue.setVatAmount(BD_VAT.multiply(v1));
    aValue.setGrossAmount(BD_GROSS.multiply(v1));

    // When
    CurrencyValue result = MathUtil.divide(aValue, bValue, 
      aValue.getType(), 4, 2, new Info());

    // Then
    assertEquals(v1.divide(v2, mc4).setScale(2, RoundingMode.HALF_UP), 
      result.getValue());
    assertEquals(BD_GROSS.multiply(v1).divide(v2, mc4).setScale(2, RoundingMode.HALF_UP), 
      result.getGrossAmount());
    assertEquals(BD_VAT.multiply(v1).divide(v2, mc4).setScale(2, RoundingMode.HALF_UP), 
      result.getVatAmount());
}
````
</div>
<div>

```java
    @Property(trials = 1000)
    public void sumValueList(List<BigDecimal> inputs) throws CalculationException {
        // Given
        List<NumericValue<?>> valueList = inputs.stream()
          .map(NumberValue::new)
          .collect(Collectors.toList());

        // When
        var result = MathUtil.sum(valueList);

        // Then
        if (inputs.isEmpty()) {
            assertTrue(result.isEmpty());
        } else {
            assertTrue(result.isPresent());
            assertEquals(inputs.stream().reduce(ZERO, BigDecimal::add), result.get().getValue());
        }
    }


```
</div>
</div>


---

# When to use Property-Based Testing?

## 😍 Property-Based Testing is great for

  - **Complex Input Spaces**: It shines in scenarios with complex input spaces or systems with numerous edge cases that are hard to enumerate.
  - **Mathematical and Logic-Based Code**: Ideal for mathematical algorithms or logic-based functionalities where properties can be clearly defined.
  - **Data Transformation and Validation**: Valuable for validating data transformations or complex validation rules within systems.

## ☔️ Property-Based Testing is not so great for

  - **Simple and Straightforward Logic**: For simpler code where the behavior is straightforward and easily verified with specific examples, property-based testing might be excessive.
  - **Legacy Code or Unfamiliar Systems**: In cases of legacy code lacking clear properties or unfamiliar systems where defining properties is challenging, it might not be as beneficial.

---


# Using PBT for Test Driven Development (TDD)

- Naive approaches to TDD often focus on starting with simple examples and then adding more complex examples. This can lead to incomplete tests, missed edge cases and may take a lot of time.

-  Property-based testing can help to drive development by generating a large number of test cases and then verifying the claimed properties.

- [Property-Driven Development](https://www.xpdays.de/2019/downloads/133-property-based-driven-development/JohannesLink-PropertyDrivenDevelopment.pdf)
- [Introduction to Properties-Driven Development](https://dev.to/meeshkan/introduction-to-properties-driven-development-547g)

---


# Your Take Aways from this Talk

### Property Based Testing... 

- is a great addition to our unit-test tool-box
- helps to thinks deeper about your problem domain and fo find useful general rules and properties
- helps to find edge-cases
- can ease test driven development (TDD) by generating meaningful test cases

### ...but
- It is not a silver bullet
- It is not a replacement for example-based testing


---

# Additional Resources and References

- **JUnit-QuickCheck**:
    - [JUnit-QuickCheck GitHub Repository](https://github.com/pholser/junit-quickcheck)
    - [Documentation and Examples](https://pholser.github.io/junit-quickcheck/site/1.0/index.html)
    - [JUnit-QuickCheck Maven Repository](https://mvnrepository.com/artifact/com.pholser/junit-quickcheck)
    - [Property-based Testing mit JUnit QuickCheck](https://www.heise.de/hintergrund/Property-based-Testing-mit-JUnit-QuickCheck-3935767.html)
    
- **PBT in General**
    - [Ho to write good Property Tests](https://research.chalmers.se/publication/517894/file/517894_Fulltext.pdf)
    - [Introduction to PBT with F#](https://fsharpforfunandprofit.com/pbt/)

- **Today's slides plus source code**:
    - [my PropertyBasedTesting Repo](https://gitlab.ista.net/architects/PropertyBasedTesting)

slides created with [Marp](https://marp.app/)


---

<!-- _class: title -->
<!-- _backgroundImage: url('ista-title-bg.png') -->
<!-- _color: white -->

# Thanks for your attention!

---

<!-- _class: title -->
# Backup Slides

---

# Properties and Universal Quantification from Math

<div class="columns-2">
<div>

$$\begin{array}{lrcl} \\ \\
\text{Commutativity:} & \forall a, b \in \mathbb{Z}, \quad a + b & = & b + a  \\ \\ \\
\text{Associativity:} & \forall a, b, c \in \mathbb{Z}, \quad  (a + b) + c & = & a + (b + c) \\ \\ \\
\text{Identity Element:} & \forall a \in \mathbb{Z}, \quad a + 0 & = & a \\ \\ \\
\text{Inverse element:} & \forall a \in \mathbb{Z}, \quad a + (-a) & = & 0 \\ \\
\end{array}$$

</div>

<div>

```java
@RunWith(JUnitQuickcheck.class)
public class AdditionPropertiesTest {

  @Property
  public void additionIsCommutative(int a, int b) {
    assertEquals("Addition is commutative", a + b, b + a);
  }

  @Property
  public void additionIsAssociative(int a, int b, int c) {
    assertEquals("Addition is associative", (a + b) + c, a + (b + c));
  }

  @Property
  public void additionWithZeroIdentity(int a) {
    assertEquals("Zero is an identity ('neutral element') for addition", a + 0, a);
  }

  @Property
  public void additionHasInverse(int a) {
    assertEquals("Every number has an inverse with respect to addition", a - a, 0);
  }
}
```
</div>

</div>

- PBT is **not proving** the properties! It rather tries to find counter-examples from a large number of random cases. If no counter example is found, the testcase is reported as passed.

- But by writing testcases that cover all parts of a full induction, we can use PBT to actually **prove** properties for all inputs. See [Section 4.4 Inductive Testing](https://research.chalmers.se/publication/517894/file/517894_Fulltext.pdf).
