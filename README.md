# Lab 6 — ArrayList for Object Data Storage

CpE 211 • Data Structures and Algorithms • Cor Jesu College, College of Engineering

## 1. Overview

This lab builds a registrar-style student record system using `ArrayList<Student>`.
Two classes hold the data and the logic, one optional class adds a Swing GUI for
interactive testing, and a console menu drives every required operation.

* **CRUD** — add, find by ID, update grade, remove by ID, display all
* **Sorting** — `Collections.sort` with anonymous `Comparator` classes (no lambdas), by
  name A–Z and by grade highest–lowest
* **Safety** — duplicate IDs are rejected, missing IDs report an error, empty-list
  display and sorts are handled

---

## 2. Files

| File | Purpose |
|---|---|
| `Student.java` | Data class: `private` fields `id`, `name`, `grade`; constructor, getters, setters, `toString()` |
| `StudentManager.java` | Holds `ArrayList<Student>` and all required methods; `main()` runs the Scanner menu |
| `StudentGUI.java` | **Bonus** — Swing `JFrame` with a `JTable` view of the same `StudentManager` (own `main()`) |
| `Lab6Tests.java` | Standalone test harness (no JUnit) — 41 PASS/FAIL checks over every required method |
| `test-input.txt` | Scripted menu input used to produce the test log (reproduces every test case) |
| `test-run.log` | Console log of a successful full run — the submission evidence |

### Required methods (as specified by the lab)

```java
public void addStudent(Student s)          // rejects duplicate IDs
public Student findById(String id)         // returns null if missing
public void updateGrade(String id, double grade)
public void removeById(String id)
public void displayAll()                   // handles empty list
public void sortByName()                   // Collections.sort + anonymous Comparator
public void sortByGrade()                  // Collections.sort + anonymous Comparator
public ArrayList<Student> getStudents()    // accessor used by the GUI
```

### Running the program

```bat
javac *.java
java StudentManager
```

GUI (bonus):

```bat
java StudentGUI
```

Running the unit test harness:

```bat
javac Lab6Tests.java StudentManager.java Student.java
java Lab6Tests
```

Reproducing the test log:

```bat
java StudentManager < test-input.txt > test-run.log
```

---

## 3. Manual Trace

ArrayList state after each operation. The list holds `Student` references; index 0 is
the leftmost element.

| Step | Operation | ArrayList after the operation |
|---|---|---|
| 0 | `new StudentManager()` | `[]` (empty) |
| 1 | `addStudent(101, Alice, 92)` | `[101 Alice 92]` |
| 2 | `addStudent(102, Bob, 88)` | `[101 Alice 92, 102 Bob 88]` |
| 3 | `addStudent(103, Charlie, 88)` | `[101 Alice 92, 102 Bob 88, 103 Charlie 88]` |
| 4 | `addStudent(104, Diana, 95)` | `[101, 102, 103, 104 Diana 95]` (4 elements) |
| 5 | `addStudent(101, Eve, 70)` → **rejected**, ID exists | unchanged — still 4 elements |
| 6 | `findById("102")` | unchanged — returns reference to `102 Bob 88`, list not modified |
| 7 | `updateGrade("104", 97)` | `[101 Alice 92, 102 Bob 88, 103 Charlie 88, 104 Diana 97]` (same 4 references, object 104 mutated in place) |
| 8 | `addStudent(106, Bob, 85)` | 5 elements: `[101, 102, 103, 104, 106 Bob 85]` |
| 9 | `removeById("101")` | `[102 Bob 88, 103 Charlie 88, 104 Diana 97, 106 Bob 85]` (elements after the hole shift left) |
| 10 | `sortByName()` | `[102 Bob, 106 Bob, 103 Charlie, 104 Diana]` — equal names `Bob`/`Bob` keep original order (stable sort) |
| 11 | `sortByGrade()` | `[104 Diana 97, 102 Bob 88, 103 Charlie 88, 106 Bob 85]` — equal grades `88`/`88` keep original order (102 before 103) |

Key observations from the trace:

* **Update does not change list length** — it mutates the `Student` object the list
  already references (step 7).
* **Remove shifts** — everything after index 0 moves one slot left (step 9).
* **Sorts reorder references only** — the `Student` objects themselves are untouched
  (steps 10–11).
* **Ties are stable** — `Collections.sort` is guaranteed stable, so equal keys appear
  in their previous relative order.

---

## 4. Test Evidence

Two forms of evidence:

1. **`Lab6Tests.java`** — automated unit-test harness, run with `java Lab6Tests`:

```text
Total checks: 41
Passed: 41
Failed: 0
RESULT: PASS
```

   Exit code `0`. It covers the `Student` class, empty-list behaviour, add +
   duplicate rejection, find found/missing, update found/missing, remove found/missing,
   display, both sorts (including equal-name and equal-grade stability), and that sorts
   reorder references without touching field values.

2. **Full console run** — `test-run.log` (produced by
   `java StudentManager < test-input.txt > test-run.log`, exit code 0).

| # | Test case | Input / action | Expected | Actual result |
|---|---|---|---|---|
| 1 | Display empty list | Menu `5` on a fresh run | "The list is empty. No students to display." | ✅ Same message printed |
| 2 | Add several students | `1` × 4 → `101/Alice/92`, `102/Bob/88`, `103/Charlie/88`, `104/Diana/95` | 4× "Student added successfully." | ✅ 4 successes, all 4 later visible in display |
| 3 | Add duplicate ID | `1` → `101/Eve/70` | Error, list unchanged | ✅ "Error: Student ID 101 already exists."; Eve not in list |
| 4 | Find existing ID | `2` → `102` | Student printed | ✅ `Found: ID: 102 \| Name: Bob \| Grade: 88.0` |
| 5 | Find missing ID | `2` → `999` | Not-found error | ✅ "Error: Student with ID 999 not found." |
| 6 | Update a record | `3` → `104`, grade `97` | Success; grade changed | ✅ "Grade updated successfully."; display later shows `104 … Grade: 97.0` |
| 7 | Update missing ID | `3` → `999`, grade `0` | Not-found error | ✅ "Error: Student with ID 999 not found." |
| 8 | Add after update | `1` → `106/Bob/85` | Success (5th record) | ✅ "Student added successfully." |
| 9 | Remove a record | `4` → `101` | Success; list shrinks | ✅ "Student removed successfully."; display shows 4 records, no 101 |
| 10 | Remove missing ID | `4` → `999` | Not-found error | ✅ "Error: Student with ID 999 not found." |
| 11 | Display all | `5` | All 4 records printed | ✅ 102, 103, 104, 106 listed between header/footer |
| 12 | Sort equal names | `6` (two records named Bob) | A–Z order; ties keep prior order | ✅ `102 Bob`, `106 Bob`, `Charlie`, `Diana` — 102 before 106, matching insertion order |
| 13 | Sort equal grades | `7` (102 and 103 both 88) | Highest→lowest; ties keep prior order | ✅ `97`, `88 (102)`, `88 (103)`, `85` — 102 before 103 |
| 14 | Exit | `0` | Clean exit | ✅ "Goodbye!", exit code 0 |

Every required case from the lab guide — add several, find existing/missing, update and
remove, sort equal grades and names — plus the empty-list and duplicate-ID edge cases
was executed and matches the expected column.

---

## 5. Data-Structure Reasoning

### Why `get(index)` is usually O(1)

`ArrayList` keeps its elements in one contiguous `Object[]` array. Getting element
`i` is direct address arithmetic:

```
address = baseAddress + i × elementSize
```

The CPU computes that in a constant number of instructions — it does not walk past
neighbouring elements. Size of the list never changes the cost, so lookup by index is
**O(1)**.

### Why inserting in the middle is O(n)

Inserting at index `i` must first **make a hole**: every element from `i` to the end
has to move one slot to the right (`System.arraycopy` does this in one native call,
but it still touches each of those elements).

| Insert position | Elements to shift | Cost |
|---|---|---|
| Index 0 (front) | all `n` | O(n) |
| Middle (`n/2`) | ~`n/2` | O(n) |
| Index `n` (end) | 0 (amortized) | O(1) amortized |

Average case shifts half the array → **O(n)**. Compare `get`: zero shifts, O(1).
That asymmetry is the whole answer to the viva question.

Related costs in this lab:

| Operation | Cost | Why |
|---|---|---|
| `addStudent` (append) | amortized O(1) | shift only when the internal array must grow |
| `findById` | O(n) | linear scan, IDs are not indexed |
| `updateGrade` | O(n) find + O(1) set | mutate the found object in place, no shift |
| `removeById` | O(n) | find the object, then shift everything after it left |
| `sort` | O(n log n) | `Collections.sort` (TimSort) |

### Fixed array vs resizable `ArrayList`

| | Fixed array `Student[]` | `ArrayList<Student>` |
|---|---|---|
| Size | fixed when created | grows/shrinks automatically |
| Wasting slots | must pre-allocate or run out | grows by ~1.5× only when full |
| Storing objects | works, but with `null` holes to manage | natural fit for CRUD on records |
| Syntax | manual length bookkeeping | `add`, `remove`, `size` built in |

The registrar scenario ("a list that can grow") is exactly the case where a fixed
array forces either an arbitrary capacity or painful resizing code — `ArrayList`
handles it internally.

### How the sorts work

Both sorts build an anonymous inner class implementing `Comparator<Student>` (the lab
requires this form rather than a lambda) and pass it to `Collections.sort`:

* **By name** — `a.getName().compareTo(b.getName())`: negative → `a` first, 0 → equal,
  positive → `b` first.
* **By grade** — manual comparison returning `-1`/`1`/`0` to order highest first.

`Collections.sort` uses TimSort, which is **stable**: records that compare equal keep
their previous relative order. The test log proves it — the two `Bob` records stay
`102` then `106`, and the two `88.0` grades stay `102` then `103`.

---

## 6. AI-Use Declaration

**Instructor-approved model:** OpenCode

**OpenCode was used for:**

* reviewing `Student.java` and `StudentManager.java` for completeness against the lab
  requirements
* creating `StudentGUI.java` (a Swing `JFrame` + `JTable` test harness) after the
  student requested "a JFrame or something so it's easy to test"
* adding the `getStudents()` accessor to `StudentManager` so the GUI can read the list
* designing and running the scripted console test (`test-input.txt` → `test-run.log`)
* drafting this README (trace, test table, reasoning, viva preparation)

**Prompts used (in order):**

1. *"in this lab6 can you create a jframe or something like that so that its easy for
   me to test"* — plan first, then implementation of the GUI
2. *"how to run this"* — compile/run commands
3. *"so i can pass this lab. like is this good enough?"* — rubric gap analysis after the
   student pasted the Lab 6 guide
4. Plan approval for: run live tests, capture the log, and write this README

**Suggestions adopted:** separate GUI file (console menu left untouched), `JTable`
display, `getStudents()` accessor, single-README submission layout.

**Suggestions rejected/modified:** none were rejected outright. The first test script
mis-aligned (a failed `updateGrade` still reads a grade line, which shifted every later
input) — the model's run produced `Invalid choice` errors in the log; the student
recognized the bad run, the input script was corrected by adding the missing grade
line, and the test was re-run to produce the clean `test-run.log` submitted here.

**Independent verification performed:**

* `javac *.java` compiles with zero errors/warnings
* `java Lab6Tests` — 41 checks, 41 passed, 0 failed, exit code 0
* full console test executed against the real menu; all 14 cases match the expected
  column (Section 4)
* edge cases hand-checked: empty list, duplicate ID, missing ID on find/update/remove,
  equal grades, equal names
* `java StudentGUI` launches without exceptions and mirrors the same operations

---

## 7. Viva Preparation

### The lab question: *Why is `get(index)` usually O(1) but insertion in the middle O(n)?*

> `ArrayList` stores its data in one contiguous array. `get(index)` computes the
> memory address directly — `base + index × elementSize` — and reads it in one step,
> so the list size doesn't matter: **O(1)**.
>
> Inserting in the middle must first make room: every element from the insertion point
> to the end shifts one position to the right, then the new value is written. On
> average that touches half of the `n` elements, worst case all of them, so **O(n)**.
> Inserting at the very end is the exception — nothing shifts — which is why appending
> is amortized O(1) (only the occasional array doubling costs O(n)).

### Likely follow-up questions

**1. Why is appending "amortized" O(1)?**
When the internal array fills up, ArrayList allocates a bigger array (~1.5×) and
copies everything — that one call is O(n). But it happens rarely: after growing to
capacity `c`, you get roughly `c/2` free appends before the next copy, so the copy
cost spread over those appends averages out to constant time per append.

**2. Why does `findById` take O(n) if `get(index)` is O(1)?**
`get(index)` is fast only when you already know the index. Finding by ID means
scanning elements one by one until the ID matches — the ID is not the index. To make
lookups fast you'd need a `HashMap` keyed by ID.

**3. What happens internally when you `remove` from the middle?**
Find the object (O(n) scan), then shift every element after the hole one slot left
(O(n)), and decrease `size`. The old reference at the end is nulled out so it isn't
leaked.

**4. Why use an anonymous `Comparator` instead of `Comparable` inside `Student`?**
Separation of concerns: the same list can be sorted multiple ways (by name, by grade)
without changing the `Student` class. `Comparable` would fix a single natural order on
the class itself.

**5. ArrayList vs LinkedList for this registrar app?**
LinkedList would make middle insert O(1) *if you already hold the node*, but finding
the position is still O(n), and array-backed lists win for `get(index)` and cache
locality. With sorting dominating the cost (O(n log n)), ArrayList is the better
default.

**6. Is the sort stable, and why does it matter here?**
Yes — `Collections.sort` (TimSort) is stable. Records with equal grades or equal names
keep their previous relative order, so the output is deterministic and doesn't jump
around between runs — visible in the log with the two `Bob` records and the two `88.0`
grades.

---

## 8. Test Evidence Summary

| Item | Result |
|---|---|
| `javac *.java` | exit 0, no errors |
| `java Lab6Tests` (unit-test harness) | 41/41 PASS, exit code 0 |
| `java StudentManager` (scripted full run) | exit 0, 14/14 cases as expected |
| `java StudentGUI` | launches without exceptions |
| Manual trace | Section 3 |
| Test table | Section 4 (14 cases) |
| Console log | `test-run.log` |
