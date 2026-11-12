# Agent Log - Lab 6

Date of work: 2026-11-09

Project folder: `C:\Users\Lance\OneDrive\Documents\vscode\Lab6`

Assistant used: OpenCode (`opencode/mimo-v2.6-flash-free`)

## Task

Complete the Lab 6 submission deliverables:

* review the existing `Student.java` and `StudentManager.java` against the lab guide
* add a Swing GUI (`StudentGUI.java`) so the student can test the operations visually
* run the required test cases and capture a console log as test evidence
* create `Lab6Tests.java`, a standalone unit-test harness
* write `README.md` with the manual trace, test-evidence table, data-structure
  reasoning, AI-use declaration, and viva preparation
* create this agent log
* initialize a Git repository, commit the deliverables, and push them to a new
  GitHub repository

## Files inspected before changes

* `Student.java` — data class with private fields, constructor, getters, setters,
  `toString()`. Already complete; not modified.
* `StudentManager.java` — `ArrayList<Student>` with all five required methods
  (`addStudent`, `findById`, `updateGrade`, `removeById`, `displayAll`), both sorts
  using anonymous `Comparator` classes, and the Scanner menu. Only one accessor was
  added (below).
* `Lab5/README.md` and `Lab5/AGENT_LOG.md` — used as the format reference for this
  lab's documents and log.
* `Lab5` Git repository (`github.com/lancebimbo-png/DSA_Lab5`) — used as the naming
  and visibility reference for the Lab 6 repository.

## Changes made

### 1. `StudentManager.java`

Added one method so the GUI and the test harness can read the list:

```java
public ArrayList<Student> getStudents() {
    return students;
}
```

No other logic was changed. The console menu and all required method signatures are
untouched.

### 2. `StudentGUI.java` (new, bonus)

A Swing `JFrame` with ID/Name/Grade fields, a non-editable `JTable` in a scroll pane
(auto-sortable column headers), buttons for Add, Find by ID, Update Grade, Remove,
Sort A-Z, Sort by Grade, and Clear, and a status label. It validates input (empty
fields, non-numeric grade, duplicate ID, missing ID) with dialogs before calling the
same `StudentManager` methods. Launched on the Event Dispatch Thread. This file is a
testing aid, not a rubric requirement.

### 3. `test-input.txt` and `test-run.log` (new)

Scripted console input covering every case required by the lab guide: empty-list
display, four adds, duplicate-ID rejection, find existing and missing ID, update
existing and missing ID, add after update, remove existing and missing ID, display,
sort by name with two equal names, sort by grade with two equal grades, and exit.
The first run mis-aligned because a failed `updateGrade` still reads a grade line;
the script was corrected (an extra grade line was added for the missing-ID update)
and re-run to produce the clean `test-run.log`.

### 4. `Lab6Tests.java` (new)

Standalone unit-test harness with no JUnit, Maven, or Gradle — plain `javac` and
`java`. It prints `PASS`/`FAIL` per check, a summary with total/passed/failed counts,
and exits with code `1` on any failure. Program chatter is suppressed by redirecting
`System.out`; the harness prints through a saved stream. Coverage: `Student`
constructor/getters/setters/`toString`, empty-list state (including empty display and
empty sorts), add and duplicate rejection, find found/missing, update found/missing,
remove found/missing, list growth after remove, populated display output, sort by
name A-Z, sort by grade highest-lowest, stable ordering for equal names and equal
grades, and that sorts reorder references without changing field values.

### 5. `README.md` (new)

Contains: overview, files table with run instructions, 11-step manual trace of the
ArrayList state, 14-case test-evidence table (Input/action, Expected, Actual) filled
from the real run, data-structure reasoning (O(1) `get(index)` vs O(n) middle insert,
amortized append, ArrayList vs fixed array, how the comparators work, stability),
the AI-use declaration, viva preparation with the lab question and six follow-up
answers, and a test-evidence summary.

### 6. `.gitignore` (new)

Ignores `*.class`, `.vscode/`, and `tests-output.txt` so build artifacts and editor
settings are not committed, matching what the Lab 5 repository excludes.

### 7. `AGENT_LOG.md` (new)

This file.

### 8. Cleanup

All generated `.class` files were deleted after testing, as required before
submission; `.gitignore` prevents them from being committed if they are regenerated.

## Commands actually run and results

### Compile

```bat
javac *.java
```

Result: exit code `0`, no compilation errors.

### Unit-test harness

```bat
java Lab6Tests
```

Result:

```text
Total checks: 44
Passed: 44
Failed: 0
RESULT: PASS
```

Exit code `0`.

### Scripted console run

```bat
java StudentManager < test-input.txt > test-run.log
```

Result: exit code `0`. All 14 documented cases matched the expected column, including
the empty list, duplicate ID, missing IDs, equal names (two Bobs kept insertion
order), and equal grades (two 88.0s kept insertion order).

### GUI smoke test

Launched `java StudentGUI` in the background, waited three seconds, confirmed the
process was still alive with no exceptions on stderr, then terminated the test
instance. Result: window launched successfully.

### Class-file cleanup

```powershell
Remove-Item -Path "*.class" -Force
```

Result: 13 `.class` files removed; the folder held only source and submission files.

### GitHub CLI installation

```bat
winget install --id GitHub.cli --accept-source-agreements --accept-package-agreements
```

Result: GitHub CLI 2.103.3 installed. The shell session needed the full path
`C:\Program Files\GitHub CLI\gh.exe`.

### GitHub authentication

```bat
gh auth login --hostname github.com --git-protocol https --web
```

Result: the first two one-time codes expired before the browser flow was completed.
A third code was approved by the student. `gh auth status` confirmed:

```text
✓ Logged in to github.com account lancebimbo-png (keyring)
  - Token scopes: 'gist', 'read:org', 'repo'
```

### Git repository and first commit

```bat
git init -b main
git add Student.java StudentManager.java StudentGUI.java Lab6Tests.java README.md test-input.txt test-run.log .gitignore
git commit -m "Complete Lab 6 ArrayList student manager"
```

Result: root commit `c82f336` on `main`, 8 files, 1302 insertions. `.class` files were
not staged.

### Repository creation and push

```bat
gh repo view lancebimbo-png/DSA_Lab5 --json visibility
gh repo create DSA_Lab6 --public --source "C:\Users\Lance\OneDrive\Documents\vscode\Lab6" --remote origin --push
```

Result: `DSA_Lab5` was checked first and is `PUBLIC`, so `DSA_Lab6` was created
public to match. The command reported:

```text
https://github.com/lancebimbo-png/DSA_Lab6
 * [new branch]      HEAD -> main
branch 'main' set up to track 'origin/main'.
```

Verification with `git status` (branch up to date, working tree clean) and
`git ls-remote --heads origin` confirmed remote `main` points at commit `c82f336`.

## Assistance used

OpenCode was used to review the lab files against the rubric, create
`StudentGUI.java`, add `getStudents()`, design and run the scripted console test,
create `Lab6Tests.java`, write `README.md` and this log, install and authenticate
the GitHub CLI, and perform the Git init, commit, repository creation, and push.

No test result was invented. Every count and exit code above came from a command run
in this session. Student review and sign-off are not claimed in this document.

## Student verification status

Student verification has **not** been performed as part of this implementation. The
student still needs to:

1. Read `README.md`, `Lab6Tests.java`, and this log.
2. Run `javac *.java` then `java Lab6Tests` and confirm `44 passed, 0 failed`.
3. Run `java StudentManager` and try the menu operations, or review `test-run.log`.
4. Study the viva question and follow-up answers in `README.md` Section 7.
5. Submit `https://github.com/lancebimbo-png/DSA_Lab6` under the corresponding
   Google Classroom assignment.

## Final revision (2026-11-12, reviewed with Claude before submission)

Defects found and fixed after the first submission commit (`c82f336`):

1. **Crash on bad grade input.** Typing a non-number at "Enter Grade" / "Enter new Grade" threw `InputMismatchException` and ended the program (`scanner.nextDouble()`). Added `StudentManager.readGrade(Scanner, String)`, which reads a line and re-asks until it parses. Menu cases 1 and 3 use it.
2. **Case-sensitive name sort.** `compareTo` ordered all uppercase-first names before lowercase ones (`Zed` before `alice`). Changed to `compareToIgnoreCase`; names equal ignoring case stay in stable order.
3. **README inconsistency.** The viva answer said "array doubling" while Section 5 says ~1.5× growth. Viva answer corrected.
4. **Missing evidence files.** `test-input.txt` and `test-run.log` were not in the reviewed folder; regenerated from the README's 14 cases. Output matches the previously documented results. Added `test-input-badgrade.txt` / `test-run-badgrade.log` for fix 1.

Tests added to `Lab6Tests.java` (no existing check changed): `sortByNameIgnoreCaseTests` (1 check) and `readGradeTests` (2 checks). Result: **44 checks, 44 passed, 0 failed**, exit 0. Scripted run `java StudentManager < test-input.txt` exit 0, output identical to the original 14-case run.

The GitHub repo still holds the earlier commit until these changes are committed and pushed by the student.
