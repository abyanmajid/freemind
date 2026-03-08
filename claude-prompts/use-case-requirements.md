# FreeMind Use Case Requirements Document

**Version:** 1.0
**Date:** 2026-03-09
**Based on:** UX Audit Report (claude-prompts/ux-audit-report.md) and independent codebase analysis

---

## Improvement 1: Inline Node Editor Does Not Wrap or Resize for Long Text

**Problem Statement:** The inline text editor (`EditNodeTextField`) uses a fixed-width `JTextField` with hard-coded minimum widths (150px for leaf/folded nodes, 50px for others). When a user types long text, the field scrolls horizontally rather than growing or wrapping. There is no visual cue that an alternate long-text editor exists (Alt+Enter).

**How to Reproduce / Current State:**
1. Launch FreeMind.
2. File > New to create a blank mind map.
3. Right-click the root node > Insert Child Node.
4. Press F2 to begin editing (or just start typing).
5. Type a string of 200+ characters.
6. Observe: the text field scrolls horizontally; text overflows without wrapping. No tooltip or indicator suggests using Alt+Enter for multi-line editing.

**Use Cases under this Improvement:** UC-1, UC-2, UC-3

---

#### Use Case 1: Auto-Wrapping Inline Text Entry

| Field | Description |
|---|---|
| Goal | Enter multi-line text directly in the inline node editor without switching to the long-node dialog |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open with at least one node selected for editing |
| Success End Condition | Typed text wraps to a new line when it reaches the node's width boundary; the text field grows vertically to accommodate content |
| Failed End Condition | Text continues to scroll horizontally in a single-line field; user is unaware of the Alt+Enter alternative |
| Actors | Mind map author |
| Trigger | User presses F2 or double-clicks a node to begin inline editing and types more text than the visible width |

#### Use Case 2: Inline Editor Dynamically Resizes Width to Match Content

| Field | Description |
|---|---|
| Goal | Have the inline editor grow horizontally (up to a configurable maximum) as the user types short text, so the visible field size matches content length |
| Scope & Level | System / Subfunction |
| Preconditions | Inline editing is active on a node with default or short text |
| Success End Condition | The text field width grows character-by-character up to a maximum width, then wraps; the node view updates in real time |
| Failed End Condition | The field remains at its fixed 150px/50px minimum regardless of content length |
| Actors | Mind map author |
| Trigger | User types or pastes text into the inline editor |

#### Use Case 3: Visual Indicator for Alt+Enter Long-Node Editor

| Field | Description |
|---|---|
| Goal | Discover the long-node editor when inline editing is insufficient |
| Scope & Level | System / User-goal |
| Preconditions | User is editing a node inline and text exceeds visible width |
| Success End Condition | A tooltip, status bar message, or subtle icon appears indicating "Press Alt+Enter for multi-line editor" |
| Failed End Condition | User never discovers the long-node editor and manually creates multiple child nodes to represent multi-line content |
| Actors | Mind map author (especially first-time user) |
| Trigger | Inline editor text exceeds the visible field width |

---

## Improvement 2: Find Dialog Lacks Replace Functionality

**Problem Statement:** The Find dialog (Ctrl+F) supports only forward text search with a single "Find in notes too" checkbox. The Edit menu contains a "Find & Replace" entry defined in `mindmap_menus.xml` that maps to an empty action placeholder — clicking it does nothing. There is no way to perform bulk text replacement across nodes.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Create 10 child nodes, each containing the word "draft".
3. Edit > Find & Replace — observe the menu item exists but does nothing.
4. Edit > Find (Ctrl+F) — observe only a search field and "Find in notes too" checkbox; no replace field.

**Use Cases under this Improvement:** UC-4, UC-5, UC-6

---

#### Use Case 4: Find and Replace Text Across All Nodes

| Field | Description |
|---|---|
| Goal | Replace all occurrences of a term across every node in the current map |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open with multiple nodes containing the search term |
| Success End Condition | All instances of the search term are replaced with the replacement text; undo can revert all replacements as a single operation |
| Failed End Condition | Replacement is not performed; user must manually edit each node |
| Actors | Mind map author |
| Trigger | User opens Edit > Find & Replace (Ctrl+H) and enters search and replacement terms, then clicks "Replace All" |

#### Use Case 5: Find and Replace with Case-Sensitive Matching

| Field | Description |
|---|---|
| Goal | Replace text only when the case matches exactly |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open; the Find & Replace dialog is displayed |
| Success End Condition | Only nodes with exact case-matched text are updated; other casing variants are left unchanged |
| Failed End Condition | Case-insensitive replacement changes unintended nodes (e.g., replacing "Draft" also changes "DRAFT") |
| Actors | Mind map author |
| Trigger | User checks "Case sensitive" in the Find & Replace dialog and clicks "Replace All" |

#### Use Case 6: Find and Replace Within Selected Branch Only

| Field | Description |
|---|---|
| Goal | Limit find-and-replace to the subtree rooted at the currently selected node |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected; the Find & Replace dialog is open |
| Success End Condition | Only nodes within the selected subtree are modified; nodes outside the subtree are untouched |
| Failed End Condition | Replacement affects the entire map despite the user intending a scoped change |
| Actors | Mind map author |
| Trigger | User selects a branch root node, opens Find & Replace, checks "Selected branch only", and clicks "Replace All" |

---

## Improvement 3: Icon Selection Grid Has No Search or Filtering

**Problem Statement:** The `IconSelectionPopupDialog` displays 100+ icons in a flat grid with no search box, no categories, and no favorites. The icon name is only shown on hover in a bottom label. Finding a specific icon requires visual scanning of the entire grid each time.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Select the root node.
3. Insert > Icons (or press the icon toolbar button).
4. Observe: a large grid of icons with no search field, no category tabs, and no way to filter by name.

**Use Cases under this Improvement:** UC-7, UC-8, UC-9

---

#### Use Case 7: Search Icons by Name

| Field | Description |
|---|---|
| Goal | Quickly find an icon by typing its name (e.g., "priority", "star", "flag") |
| Scope & Level | System / User-goal |
| Preconditions | The icon selection dialog is open |
| Success End Condition | The grid filters in real time to show only icons whose names match the search text |
| Failed End Condition | User must visually scan 100+ icons to find the desired one |
| Actors | Mind map author |
| Trigger | User types text into a search field at the top of the icon selection dialog |

#### Use Case 8: Favorite Icons Quick-Access

| Field | Description |
|---|---|
| Goal | Access frequently used icons without scrolling through the full grid |
| Scope & Level | System / User-goal |
| Preconditions | The user has previously marked one or more icons as "favorites" |
| Success End Condition | A "Favorites" row appears at the top of the icon dialog showing the user's most-used or pinned icons |
| Failed End Condition | User must locate the same icons in the grid every time, even for icons used dozens of times |
| Actors | Mind map author |
| Trigger | User opens the icon selection dialog |

#### Use Case 9: Icon Categories for Organized Browsing

| Field | Description |
|---|---|
| Goal | Browse icons by logical category (e.g., Priorities, Arrows, People, Status) |
| Scope & Level | System / User-goal |
| Preconditions | The icon selection dialog is open |
| Success End Condition | Icons are grouped under labeled tabs or collapsible sections; user can expand a category to see only its icons |
| Failed End Condition | All 100+ icons remain in a single flat grid with no visual grouping |
| Actors | Mind map author |
| Trigger | User clicks a category tab or expands a category header in the icon selection dialog |

---

## Improvement 4: Encryption Uses Cryptographically Broken DES Algorithm

**Problem Statement:** Node encryption in `EncryptedMindMapNode` uses `SingleDesEncrypter` (DES, 56-bit key), `Math.random()` for salt generation (predictable linear congruential generator), and MD5 for key derivation. The minimum password length is 2 characters. Password comparison uses early-exit character comparison vulnerable to timing attacks. This is confirmed in `Tools.java` line 731.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Add a child node > right-click > Encrypt Node.
3. Enter password "ab" (2 characters — accepted as valid).
4. The node is "encrypted" with DES, which is brute-forceable in hours on modern hardware.
5. Examine `Tools.java:731`: `newSalt[i] = (byte) (Math.random() * 256l - 128l);`

**Use Cases under this Improvement:** UC-10, UC-11, UC-12, UC-13

---

#### Use Case 10: Encrypt Node with AES-256-GCM

| Field | Description |
|---|---|
| Goal | Protect sensitive node content with modern, industry-standard encryption |
| Scope & Level | System / User-goal |
| Preconditions | A node with content exists in the mind map |
| Success End Condition | Node content is encrypted using AES-256-GCM with a cryptographically random IV, PBKDF2 key derivation, and SecureRandom salt; the encrypted payload includes an algorithm version tag for future migration |
| Failed End Condition | Encryption fails and the node content is left in plaintext; user is notified of the failure |
| Actors | Mind map author |
| Trigger | User right-clicks a node and selects "Encrypt Node", then enters a password meeting the minimum strength requirement |

#### Use Case 11: Enforce Minimum Password Strength for Encryption

| Field | Description |
|---|---|
| Goal | Prevent users from setting trivially weak passwords for encrypted nodes |
| Scope & Level | System / Subfunction |
| Preconditions | User has initiated node encryption |
| Success End Condition | Passwords shorter than 8 characters or failing a basic strength check are rejected with a clear message explaining requirements |
| Failed End Condition | A 2-character password is accepted (current behaviour), providing false sense of security |
| Actors | Mind map author |
| Trigger | User submits a password in the encryption dialog |

#### Use Case 12: Migrate Legacy DES-Encrypted Nodes to AES-256

| Field | Description |
|---|---|
| Goal | Upgrade existing DES-encrypted nodes to modern AES-256-GCM encryption without data loss |
| Scope & Level | System / User-goal |
| Preconditions | A mind map contains one or more nodes encrypted with the legacy DES algorithm |
| Success End Condition | User is prompted to re-encrypt legacy nodes; upon providing the old password, content is decrypted and re-encrypted with AES-256-GCM under a new password |
| Failed End Condition | Migration fails gracefully; the node remains in its legacy-encrypted state; user is informed of the failure |
| Actors | Mind map author |
| Trigger | User opens a map containing DES-encrypted nodes, or manually initiates migration via a menu action |

#### Use Case 13: Display Encryption Strength Indicator

| Field | Description |
|---|---|
| Goal | Inform the user of the encryption algorithm and strength protecting their data |
| Scope & Level | System / Subfunction |
| Preconditions | A node is encrypted |
| Success End Condition | The encryption dialog or node tooltip displays the algorithm name (e.g., "AES-256-GCM"), key derivation method, and a visual strength indicator |
| Failed End Condition | User has no way to know what algorithm is used or whether their data is adequately protected (current behaviour) |
| Actors | Mind map author |
| Trigger | User hovers over an encrypted node or opens its properties |

---

## Improvement 5: File Save Leaks File Descriptors on Serialization Failure

**Problem Statement:** In `MindMapMapModel.saveInternal()` (line 262–284), a `FileOutputStream` wrapped in `BufferedWriter` is created but never closed — neither in a `finally` block nor via try-with-resources. If `getXml()` throws during serialization, the file handle leaks. Repeated save failures can exhaust file descriptors.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > create a map.
2. Save to a file path.
3. Externally make the file read-only or corrupt the map structure to force `getXml()` to throw.
4. Attempt File > Save — an error dialog appears but the `FileOutputStream` is never closed.
5. See `MindMapMapModel.java` lines 273–275: `BufferedWriter fileout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))); getXml(fileout);` — no close.

**Use Cases under this Improvement:** UC-14, UC-15, UC-16

---

#### Use Case 14: Save Map with Proper Resource Cleanup

| Field | Description |
|---|---|
| Goal | Save the mind map to disk with guaranteed file handle closure regardless of success or failure |
| Scope & Level | System / Subfunction |
| Preconditions | A mind map is open with unsaved changes |
| Success End Condition | The file is written successfully and the output stream is closed; no file descriptors leak |
| Failed End Condition | If serialization fails, the output stream is still closed; an error message is shown to the user; no file descriptors leak |
| Actors | Mind map author, Operating System |
| Trigger | User presses Ctrl+S or selects File > Save |

#### Use Case 15: Detect and Report Write Failures on Save

| Field | Description |
|---|---|
| Goal | Receive a clear error message when saving fails (e.g., disk full, permission denied, I/O error) |
| Scope & Level | System / User-goal |
| Preconditions | A mind map has unsaved changes; the target file path is not writable |
| Success End Condition | A user-friendly error dialog specifies the cause of failure (e.g., "Permission denied: /path/to/file.mm") and suggests corrective action (e.g., "Save As to a different location") |
| Failed End Condition | Save fails silently or with a generic Java exception stack trace (current behaviour in some paths) |
| Actors | Mind map author |
| Trigger | User attempts File > Save to an unwritable location |

#### Use Case 16: Atomic Save with Temporary File and Rename

| Field | Description |
|---|---|
| Goal | Prevent map file corruption if the application crashes or power is lost during save |
| Scope & Level | System / Subfunction |
| Preconditions | A mind map is open with unsaved changes |
| Success End Condition | The map is first written to a temporary file in the same directory, then atomically renamed to the target filename; the original file is intact until the new file is fully written |
| Failed End Condition | Writing the temporary file fails; the original map file remains untouched; user is notified |
| Actors | Mind map author, File System |
| Trigger | User saves the map (Ctrl+S or File > Save) |

---

## Improvement 6: File Lock Reader Never Closed on Normal Code Path

**Problem Statement:** In `MindMapMapModel.LockManager.tryToLock()` (line 449–463), a `BufferedReader` reading the semaphore file is closed only on one branch (stale lock at line 459) but never closed on the normal return path (line 463: `return lockingUser`). This leaks a file descriptor and on Windows prevents the semaphore file from being deleted.

**How to Reproduce / Current State:**
1. Open a `.mm` file while another FreeMind instance has it locked.
2. The lock detection reads the semaphore file and finds a valid, non-stale lock.
3. It returns `lockingUser` without closing the `BufferedReader`.
4. On Windows, the semaphore file remains locked by the JVM and cannot be deleted even after the locking instance exits.

**Use Cases under this Improvement:** UC-17, UC-18, UC-19

---

#### Use Case 17: File Lock Detection with Proper Reader Cleanup

| Field | Description |
|---|---|
| Goal | Detect whether a map file is locked by another user/instance without leaking file handles |
| Scope & Level | System / Subfunction |
| Preconditions | A `.mm` file exists with a corresponding semaphore lock file |
| Success End Condition | The semaphore file is read, the locking user information is returned, and the reader is closed on all code paths |
| Failed End Condition | The reader is left open (current behaviour on the non-stale-lock path), preventing semaphore file deletion on Windows |
| Actors | FreeMind application, File System |
| Trigger | User opens a `.mm` file that has an existing lock semaphore |

#### Use Case 18: Release Stale Lock Files Reliably on Windows

| Field | Description |
|---|---|
| Goal | Clean up stale semaphore files even when previous instances terminated abnormally |
| Scope & Level | System / Subfunction |
| Preconditions | A semaphore file exists from a previous FreeMind session that crashed or was force-quit |
| Success End Condition | The stale lock is detected (beyond the safety period), the semaphore file is successfully deleted, and a new lock is acquired |
| Failed End Condition | The semaphore file cannot be deleted because a leaked file handle holds it open (Windows-specific issue) |
| Actors | FreeMind application, File System |
| Trigger | User opens a `.mm` file with a stale lock |

#### Use Case 19: Inform User of Lock Holder Identity

| Field | Description |
|---|---|
| Goal | Show a clear message identifying who holds the lock and when it was acquired |
| Scope & Level | System / User-goal |
| Preconditions | A `.mm` file is locked by another user |
| Success End Condition | A dialog shows the locking user's name, the lock timestamp, and offers options: "Open Read-Only", "Force Unlock", "Cancel" |
| Failed End Condition | A generic error or cryptic message is displayed without lock holder identity |
| Actors | Mind map author |
| Trigger | User attempts to open a locked `.mm` file |

---

## Improvement 7: JoinNodes Corrupts HTML Content via Flawed Regex

**Problem Statement:** `JoinNodesAction.addContent()` (line 80–107 of `JoinNodesAction.java`) uses regex-based `<body>` tag manipulation to merge HTML content from multiple nodes. The regex `Pattern.compile("<body>", CASE_INSENSITIVE)` does not account for `<body>` tags with attributes (e.g., `<body style="...">`). The `split(..., -2)` call can produce unexpected fragment counts, resulting in malformed HTML.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Create 3 sibling child nodes.
3. Set each to "Use Rich Formatting" (Alt+R on each).
4. Add styled text to each node.
5. Select all three > Tools > Join Nodes.
6. Observe: merged content may have broken HTML with missing or duplicated body tags.

**Use Cases under this Improvement:** UC-20, UC-21, UC-22

---

#### Use Case 20: Join Plain-Text Nodes into a Single Node

| Field | Description |
|---|---|
| Goal | Merge multiple sibling plain-text nodes into a single node with concatenated text |
| Scope & Level | System / User-goal |
| Preconditions | Two or more sibling nodes are selected; none have children; all are in plain-text mode |
| Success End Condition | All selected nodes are merged into the first node with text separated by spaces; the other nodes are deleted; the operation is undoable |
| Failed End Condition | Text is lost, duplicated, or garbled during the merge |
| Actors | Mind map author |
| Trigger | User selects sibling nodes and invokes Tools > Join Nodes |

#### Use Case 21: Join Rich-Text (HTML) Nodes Without Corrupting Markup

| Field | Description |
|---|---|
| Goal | Merge multiple sibling rich-text nodes while preserving valid HTML structure |
| Scope & Level | System / User-goal |
| Preconditions | Two or more sibling nodes are selected; all use rich formatting (HTML content); none have children |
| Success End Condition | HTML content from all nodes is merged using proper DOM manipulation (not regex splitting); the resulting node contains valid, well-formed HTML with all formatting preserved |
| Failed End Condition | Merged content has broken HTML — duplicate `<body>` tags, missing closing tags, or lost formatting (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects rich-text sibling nodes and invokes Tools > Join Nodes |

#### Use Case 22: Join Mixed Plain-Text and Rich-Text Nodes

| Field | Description |
|---|---|
| Goal | Merge sibling nodes where some are plain text and others are rich text |
| Scope & Level | System / User-goal |
| Preconditions | Two or more sibling nodes are selected with a mix of plain-text and HTML modes |
| Success End Condition | Plain-text nodes are wrapped in `<p>` tags and incorporated into a valid HTML structure; the result is a single rich-text node with all content preserved |
| Failed End Condition | Plain-text content is injected raw into the HTML body, breaking the document structure |
| Actors | Mind map author |
| Trigger | User selects mixed-format sibling nodes and invokes Tools > Join Nodes |

---

## Improvement 8: EditServer Socket Connections Never Closed

**Problem Statement:** In `EditServer.java` (line 150–161), the `finally` block that should close client socket connections is entirely commented out. Each incoming IPC connection leaves an unclosed socket, eventually exhausting available file descriptors or ports.

**How to Reproduce / Current State:**
1. Launch FreeMind (the EditServer starts automatically on a random localhost port).
2. Use external scripts to send commands to FreeMind's edit server port repeatedly.
3. Observe: each connection's socket is never closed (the close code is commented out at line 155–161).
4. Over many connections, file descriptor or port exhaustion occurs.

**Use Cases under this Improvement:** UC-23, UC-24, UC-25

---

#### Use Case 23: Close Client Sockets After IPC Request Processing

| Field | Description |
|---|---|
| Goal | Properly release socket resources after each inter-process communication request |
| Scope & Level | System / Subfunction |
| Preconditions | The EditServer is running and accepting connections |
| Success End Condition | Each client socket is closed in a `finally` block after the request is processed, regardless of whether authentication succeeded or failed |
| Failed End Condition | Client sockets remain open indefinitely (current behaviour), leaking resources |
| Actors | FreeMind EditServer, External client application |
| Trigger | An external application connects to the EditServer port |

#### Use Case 24: Rate-Limit EditServer Connections Against DoS

| Field | Description |
|---|---|
| Goal | Prevent denial-of-service via rapid connection attempts to the EditServer |
| Scope & Level | System / Subfunction |
| Preconditions | The EditServer is running on localhost |
| Success End Condition | Connection rate is limited; connections exceeding the threshold are rejected with socket closure; a warning is logged |
| Failed End Condition | Unlimited connections are accepted, exhausting system resources |
| Actors | External client (potentially malicious), FreeMind EditServer |
| Trigger | More than N connections per second are received |

#### Use Case 25: Validate EditServer Authentication Key Using Constant-Time Comparison

| Field | Description |
|---|---|
| Goal | Prevent timing-based authentication key guessing on the EditServer |
| Scope & Level | System / Subfunction |
| Preconditions | An external client connects to the EditServer and sends an authentication key |
| Success End Condition | The key is compared using a constant-time algorithm (e.g., `MessageDigest.isEqual`) so that the comparison duration does not reveal partial key matches |
| Failed End Condition | An attacker can iteratively guess the key using timing differences (current simple `!=` comparison at `EditServer.java` line 201) |
| Actors | External client, FreeMind EditServer |
| Trigger | Client sends 4-byte authentication key to the EditServer |

---

## Improvement 9: Auto-Save Failures Are Silent — No User Notification

**Problem Statement:** The `DoAutomaticSave` timer task (in `MindMapMapModel.java`, lines 533+) catches exceptions during auto-save but only writes to `System.err` — no user-visible notification appears. If the auto-save directory becomes full, read-only, or inaccessible, users believe their work is being auto-saved when it is not.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > create a map with content.
2. Edit extensively to trigger auto-save.
3. Externally fill the temp directory or make it read-only.
4. Continue editing — observe no warning that auto-save has failed.
5. If FreeMind crashes, the user discovers no auto-save file exists.

**Use Cases under this Improvement:** UC-26, UC-27, UC-28

---

#### Use Case 26: Display Auto-Save Status Indicator in Status Bar

| Field | Description |
|---|---|
| Goal | See at a glance whether auto-save is active, when the last successful save occurred, and when the next save is scheduled |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open with auto-save enabled |
| Success End Condition | A status bar widget shows a green indicator with "Last auto-save: HH:MM:SS" text; the icon turns red if the last auto-save failed |
| Failed End Condition | Auto-save runs silently with no visual feedback (current behaviour) |
| Actors | Mind map author |
| Trigger | Auto-save timer fires or auto-save configuration changes |

#### Use Case 27: Alert User on Auto-Save Failure

| Field | Description |
|---|---|
| Goal | Be notified immediately when an auto-save attempt fails |
| Scope & Level | System / User-goal |
| Preconditions | Auto-save is enabled; the auto-save target directory is inaccessible or full |
| Success End Condition | A non-modal notification appears explaining the failure and suggesting corrective action (e.g., "Auto-save failed: disk full. Please save manually.") |
| Failed End Condition | Error is only logged to stderr; user is unaware their work is unprotected (current behaviour) |
| Actors | Mind map author |
| Trigger | Auto-save timer fires and the save operation throws an exception |

#### Use Case 28: Browse and Restore from Auto-Save History

| Field | Description |
|---|---|
| Goal | View a list of auto-save backups and restore from a specific version |
| Scope & Level | System / User-goal |
| Preconditions | At least one auto-save backup file exists (pattern: `FM_<mapname>_*.mm` in the temp directory) |
| Success End Condition | A dialog lists available auto-save files with timestamps and sizes; user can select one and open it in a new tab for comparison or direct restoration |
| Failed End Condition | User must manually navigate the temp directory to find backup files (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects File > Restore from Auto-Save or a similar menu action |

---

## Improvement 10: 77+ Empty Catch Blocks Silently Swallow Exceptions

**Problem Statement:** The codebase contains 77+ empty catch blocks that silently swallow exceptions, including critical paths like encryption failure (`EncryptNode.java`), lock file parsing (`MindMapMapModel.java`), and node folding (`FindAction.java` line 289). This masks bugs, causes silent data loss, and makes debugging nearly impossible.

**How to Reproduce / Current State:**
1. Trigger any of the following: encryption with a missing crypto provider, opening a map with a corrupt lock file, or performing a find-next on a map where nodes have been deleted.
2. No error is shown; the operation silently fails or produces unexpected results.
3. Confirm via grep: `catch\s*\([^)]*\)\s*\{\s*\}` returns matches across dozens of files.

**Use Cases under this Improvement:** UC-29, UC-30, UC-31

---

#### Use Case 29: Log and Report Encryption Errors to User

| Field | Description |
|---|---|
| Goal | Be informed when node encryption or decryption fails |
| Scope & Level | System / User-goal |
| Preconditions | User has initiated encryption or decryption of a node |
| Success End Condition | If the crypto operation fails (e.g., `BadPaddingException`, `IllegalBlockSizeException`), a dialog explains the failure; the node remains in its pre-operation state |
| Failed End Condition | The encrypt method returns `null` silently (current behaviour at `Tools.java` line 741–745), and the caller may store null as the encrypted content |
| Actors | Mind map author |
| Trigger | Encryption or decryption operation throws an exception |

#### Use Case 30: Handle File Lock Parse Errors Gracefully

| Field | Description |
|---|---|
| Goal | Recover gracefully when a lock semaphore file is corrupt or malformed |
| Scope & Level | System / Subfunction |
| Preconditions | A `.mm` file has a semaphore lock file with corrupt content (e.g., missing timestamp line) |
| Success End Condition | The corrupt lock is treated as stale and removed; the map opens normally; a log entry records the corruption |
| Failed End Condition | The empty catch block (at `MindMapMapModel.java` line 464) silently ignores the `NumberFormatException`, leading to unpredictable lock behaviour |
| Actors | FreeMind application, File System |
| Trigger | User opens a `.mm` file with a corrupt lock semaphore |

#### Use Case 31: Surface Exception Details in Debug/Verbose Mode

| Field | Description |
|---|---|
| Goal | Allow developers and advanced users to see caught exceptions that are normally suppressed |
| Scope & Level | System / Subfunction |
| Preconditions | FreeMind is running with a "verbose" or "debug" flag enabled |
| Success End Condition | All previously empty catch blocks log the exception with stack trace to the application log; critical exceptions also show a non-modal notification |
| Failed End Condition | Exceptions continue to be silently swallowed with no diagnostic output |
| Actors | Developer, Advanced user |
| Trigger | Any catch block that was previously empty is reached |

---

## Improvement 11: Undo/Redo Has No Visual History or State Preview

**Problem Statement:** Undo (Ctrl+Z) and Redo (Ctrl+Y) operate as a simple stack with no visual history list. Users cannot see what action they are about to undo, how many undo steps remain, or jump to a specific past state. The undo stack silently truncates at 100 entries (configurable in properties) with no warning.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Perform 15 distinct edits (add nodes, rename, reformat).
3. Press Ctrl+Z repeatedly.
4. Observe: no indicator shows what action was undone, how many steps remain, or a history panel.

**Use Cases under this Improvement:** UC-32, UC-33, UC-34

---

#### Use Case 32: View Undo/Redo History in a Side Panel

| Field | Description |
|---|---|
| Goal | See a chronological list of all undoable actions with descriptions |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open with at least one undoable edit |
| Success End Condition | A side panel or dropdown shows a list of actions (e.g., "Add node 'Marketing'", "Change font to Bold", "Delete node 'Budget'") with the current position highlighted; clicking an entry jumps to that state |
| Failed End Condition | User has no visibility into the undo stack (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens View > Undo History or clicks the undo dropdown arrow |

#### Use Case 33: Undo/Redo Status Indicator in Toolbar

| Field | Description |
|---|---|
| Goal | Know at a glance whether undo/redo is available and what action will be undone/redone |
| Scope & Level | System / Subfunction |
| Preconditions | A mind map is open |
| Success End Condition | The undo button tooltip shows "Undo: [action description]"; the redo button tooltip shows "Redo: [action description]"; buttons are greyed out when no undo/redo is available |
| Failed End Condition | Undo/redo buttons show generic tooltips with no indication of what will happen (current behaviour) |
| Actors | Mind map author |
| Trigger | User hovers over the undo or redo toolbar button |

#### Use Case 34: Warn When Approaching Undo Stack Limit

| Field | Description |
|---|---|
| Goal | Be notified before the oldest undo entries are discarded due to the stack size limit |
| Scope & Level | System / Subfunction |
| Preconditions | The undo stack has reached 90% of its configured maximum (default 100) |
| Success End Condition | A status bar warning appears: "Undo history is nearly full (95/100). Oldest entries will be discarded." |
| Failed End Condition | Old undo entries are silently discarded with no notification (current behaviour) |
| Actors | Mind map author |
| Trigger | Undo stack reaches the warning threshold |

---

## Improvement 12: Underline Formatting Action Has No Visual Effect

**Problem Statement:** `UnderlinedAction` is defined as a Java class with a keyboard shortcut (`keystroke_node_toggle_underlined`), and the `NodeAdapter` stores an `underlined` boolean field (line 122). However, the view layer never renders underlined text. A code comment in `StylePattern.java` line 436 has the `setUnderlined` call commented out.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add a child node.
2. Format > toggle underline (if discoverable via shortcut or scripting).
3. Observe: no visual change on the node. The model flag is set but the view ignores it.

**Use Cases under this Improvement:** UC-35, UC-36, UC-37

---

#### Use Case 35: Apply Underline Formatting to Node Text

| Field | Description |
|---|---|
| Goal | Render selected node text with an underline decoration |
| Scope & Level | System / User-goal |
| Preconditions | A node is selected in the mind map |
| Success End Condition | The node text is rendered with an underline; the formatting persists when saved and reloaded |
| Failed End Condition | The underline flag is set in the model but the node text renders without underline (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses the underline keyboard shortcut or selects Format > Underline |

#### Use Case 36: Toggle Underline Off for a Previously Underlined Node

| Field | Description |
|---|---|
| Goal | Remove underline formatting from a node |
| Scope & Level | System / Subfunction |
| Preconditions | A node has underline formatting applied |
| Success End Condition | The underline is visually removed; the model flag is toggled off |
| Failed End Condition | Since underline never rendered, toggling it off has no visible effect (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses the underline shortcut on an already-underlined node |

#### Use Case 37: Include Underline in Style Patterns

| Field | Description |
|---|---|
| Goal | Save and apply underline formatting as part of a reusable style pattern (F1–F9) |
| Scope & Level | System / Subfunction |
| Preconditions | The user is editing a style pattern |
| Success End Condition | The pattern includes an underline setting; applying the pattern to a node renders it with underline |
| Failed End Condition | The `setUnderlined` call in `StylePattern.java` remains commented out; patterns cannot include underline |
| Actors | Mind map author |
| Trigger | User creates or edits a style pattern and checks the "Underline" option |

---

## Improvement 13: No Visual Tab Bar for Multiple Open Maps

**Problem Statement:** Multiple open maps are managed via the Maps menu with radio button selection. There is no visual tab bar, no drag-to-reorder, no close button per tab, and no modified indicator. Switching maps requires opening a menu and clicking.

**How to Reproduce / Current State:**
1. Launch FreeMind.
2. File > Open — open 3 different `.mm` files.
3. Observe: the Maps menu shows radio buttons, but there is no persistent tab bar at the top of the window.

**Use Cases under this Improvement:** UC-38, UC-39, UC-40

---

#### Use Case 38: Switch Between Open Maps via Tab Bar

| Field | Description |
|---|---|
| Goal | Quickly switch between open maps by clicking a visible tab |
| Scope & Level | System / User-goal |
| Preconditions | Two or more maps are open |
| Success End Condition | A tab bar at the top of the editor area shows one tab per open map; clicking a tab switches to that map instantly |
| Failed End Condition | User must open the Maps menu and select from radio buttons (current behaviour) |
| Actors | Mind map author |
| Trigger | User clicks a tab in the tab bar |

#### Use Case 39: Close Individual Map from Tab Bar

| Field | Description |
|---|---|
| Goal | Close a specific map without affecting other open maps |
| Scope & Level | System / User-goal |
| Preconditions | Two or more maps are open; the tab bar is visible |
| Success End Condition | Each tab has a close button (×); clicking it prompts to save if modified, then closes only that map; other tabs remain |
| Failed End Condition | User must use File > Close, which may not clearly indicate which map will be closed |
| Actors | Mind map author |
| Trigger | User clicks the × button on a tab |

#### Use Case 40: Show Unsaved-Changes Indicator on Tab

| Field | Description |
|---|---|
| Goal | See at a glance which open maps have unsaved changes |
| Scope & Level | System / Subfunction |
| Preconditions | Multiple maps are open; some have been modified since last save |
| Success End Condition | Modified maps show an asterisk (*) or dot indicator on their tab; the indicator clears when the map is saved |
| Failed End Condition | No visual distinction between saved and unsaved maps in the Maps menu (current behaviour) |
| Actors | Mind map author |
| Trigger | User modifies a map or saves a map |

---

## Improvement 14: Clipboard Owner Set to Null Breaks Ownership Protocol

**Problem Statement:** `MindMapController.setClipboardContents()` (line 2370) calls `clipboard.setContents(t, null)`, passing `null` as the clipboard owner. FreeMind never receives `lostOwnership` notifications, violating the Java clipboard ownership protocol and potentially causing stale data issues.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add nodes.
2. Copy a node (Ctrl+C).
3. Switch to another application and copy different content.
4. Switch back to FreeMind and paste (Ctrl+V).
5. Observe: in edge cases, stale or corrupted data may be pasted because FreeMind never knew it lost clipboard ownership.

**Use Cases under this Improvement:** UC-41, UC-42, UC-43

---

#### Use Case 41: Register Clipboard Owner for Copy Operations

| Field | Description |
|---|---|
| Goal | Properly track clipboard ownership so FreeMind knows when its clipboard content is superseded |
| Scope & Level | System / Subfunction |
| Preconditions | User copies a node or text from FreeMind |
| Success End Condition | FreeMind registers itself as the clipboard owner; when another application claims the clipboard, FreeMind's `lostOwnership()` callback fires and internal state is updated |
| Failed End Condition | Clipboard owner is `null` (current behaviour); FreeMind never knows its clipboard content was replaced |
| Actors | Mind map author, Operating System clipboard |
| Trigger | User presses Ctrl+C or invokes Edit > Copy |

#### Use Case 42: Handle Lost Clipboard Ownership Gracefully

| Field | Description |
|---|---|
| Goal | Avoid pasting stale FreeMind-internal data when the system clipboard has been claimed by another application |
| Scope & Level | System / Subfunction |
| Preconditions | FreeMind copied node data; another application subsequently copied different data to the clipboard |
| Success End Condition | FreeMind detects via `lostOwnership` that its clipboard data is stale and falls back to requesting data from the system clipboard |
| Failed End Condition | Stale FreeMind-internal clipboard data is pasted instead of the most recent clipboard content |
| Actors | Mind map author, External application |
| Trigger | User pastes after another application has claimed the clipboard |

#### Use Case 43: Paste External Content as New Nodes

| Field | Description |
|---|---|
| Goal | Paste text or HTML from an external application and have it create appropriate node(s) |
| Scope & Level | System / User-goal |
| Preconditions | External application content is on the system clipboard; a node is selected in FreeMind |
| Success End Condition | External text is pasted as a child node; HTML is parsed into a tree of nodes if possible; the paste operation is undoable |
| Failed End Condition | Paste produces garbled content or a NullPointerException due to clipboard state mismatch |
| Actors | Mind map author |
| Trigger | User presses Ctrl+V with external clipboard content |

---

## Improvement 15: Paste with No Node Selected Causes NullPointerException

**Problem Statement:** `PasteAction.actionPerformed()` (line 52–56 of `PasteAction.java`) calls `getSelected()` and passes the result directly to `paste()` with no null check. If no node is selected, a `NullPointerException` occurs.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Click on empty canvas to deselect all nodes (if possible).
3. Press Ctrl+V.
4. Observe: potential `NullPointerException` crash.

**Use Cases under this Improvement:** UC-44, UC-45, UC-46

---

#### Use Case 44: Guard Paste Action Against Null Selection

| Field | Description |
|---|---|
| Goal | Prevent crash when pasting with no node selected |
| Scope & Level | System / Subfunction |
| Preconditions | No node is selected in the mind map view; clipboard contains content |
| Success End Condition | The paste action is disabled or shows a tooltip "Select a node to paste into"; no crash occurs |
| Failed End Condition | `NullPointerException` crashes the paste operation (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses Ctrl+V with no node selected |

#### Use Case 45: Disable Paste Action When No Node Is Selected

| Field | Description |
|---|---|
| Goal | Grey out the paste button and menu item when no valid paste target exists |
| Scope & Level | System / Subfunction |
| Preconditions | The mind map view is active but no node is selected |
| Success End Condition | Edit > Paste is greyed out; Ctrl+V has no effect; toolbar paste button is disabled |
| Failed End Condition | Paste remains clickable in all states regardless of selection (current behaviour) |
| Actors | Mind map author |
| Trigger | Selection changes to empty (no node selected) |

#### Use Case 46: Paste into Root Node as Fallback When No Node Selected

| Field | Description |
|---|---|
| Goal | Paste clipboard content as a child of the root node if no specific node is selected |
| Scope & Level | System / User-goal |
| Preconditions | No node is explicitly selected; clipboard contains content; a mind map is open |
| Success End Condition | Content is pasted as a child of the root node; the new node is selected |
| Failed End Condition | NullPointerException occurs (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses Ctrl+V with no node selected |

---

## Improvement 16: Preferences Panel Is Overwhelming with No Search or Grouping

**Problem Statement:** The `OptionPanel` (1373 lines in `OptionPanel.java`) presents 200+ configuration options across categories with no visual grouping within categories, no collapsible sections, no "search settings" functionality, and some options displayed with cryptic property-key-based labels (e.g., `el__enter_confirms_by_default`).

**How to Reproduce / Current State:**
1. Launch FreeMind > Tools > Preferences.
2. Navigate to the "Appearance" tab.
3. Observe: a long scrollable list of options with no hierarchy, no grouping, and some cryptic labels.

**Use Cases under this Improvement:** UC-47, UC-48, UC-49

---

#### Use Case 47: Search Preferences by Keyword

| Field | Description |
|---|---|
| Goal | Quickly find a specific preference setting by typing a keyword |
| Scope & Level | System / User-goal |
| Preconditions | The Preferences dialog is open |
| Success End Condition | A search field at the top filters the visible options in real time; only matching options and their containing sections are shown |
| Failed End Condition | User must scroll through 200+ options to find a specific setting (current behaviour) |
| Actors | Mind map author |
| Trigger | User types in the preferences search field |

#### Use Case 48: Group Related Preferences with Collapsible Sections

| Field | Description |
|---|---|
| Goal | Visually group related settings within each preferences tab |
| Scope & Level | System / User-goal |
| Preconditions | The Preferences dialog is open |
| Success End Condition | Settings within each tab are organized into labeled, collapsible sections (e.g., "Font Settings", "Color Settings", "Save Behaviour"); sections can be expanded or collapsed individually |
| Failed End Condition | All options in a tab are displayed in a flat list (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens a preferences tab |

#### Use Case 49: Display Human-Readable Labels for All Preferences

| Field | Description |
|---|---|
| Goal | Understand every preference option without knowing internal property key names |
| Scope & Level | System / Subfunction |
| Preconditions | The Preferences dialog is open |
| Success End Condition | Every option label is a clear, human-readable phrase; tooltip shows the internal property key for advanced users; no raw property keys are visible as labels |
| Failed End Condition | Some options display internal keys like `el__enter_confirms_by_default` (current behaviour) |
| Actors | Mind map author |
| Trigger | User views the Preferences dialog |

---

## Improvement 17: Zoom Controls Lack Custom Percentage Entry

**Problem Statement:** The zoom combobox offers fixed levels (50%, 75%, 100%, 125%, 150%, 200%) plus a "user defined" option. There is no way to type an arbitrary zoom level directly. Alt+Up/Down step through the fixed list only.

**How to Reproduce / Current State:**
1. Launch FreeMind.
2. Observe the zoom dropdown in the toolbar.
3. Try typing "110" — no way to enter a custom value.
4. Use Alt+Up/Alt+Down — only steps through the fixed list.

**Use Cases under this Improvement:** UC-50, UC-51, UC-52

---

#### Use Case 50: Enter Arbitrary Zoom Percentage

| Field | Description |
|---|---|
| Goal | Set the map zoom to any percentage value (e.g., 110%, 133%, 175%) |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open |
| Success End Condition | User types a number into the zoom combobox and presses Enter; the map view updates to that exact zoom level |
| Failed End Condition | Only predefined zoom levels are available (current behaviour) |
| Actors | Mind map author |
| Trigger | User clicks the zoom combobox and types a custom percentage |

#### Use Case 51: Zoom to Fit Entire Map in View

| Field | Description |
|---|---|
| Goal | Automatically adjust zoom to show the entire mind map within the visible window area |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open; some nodes may be outside the visible area |
| Success End Condition | The zoom level adjusts so all visible (unfolded) nodes fit within the window; the map is centered |
| Failed End Condition | User must manually try different zoom levels to see the whole map |
| Actors | Mind map author |
| Trigger | User selects View > Zoom to Fit or presses a keyboard shortcut |

#### Use Case 52: Zoom to Fit Selected Branch

| Field | Description |
|---|---|
| Goal | Zoom to show a specific branch and its descendants filling the viewport |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected |
| Success End Condition | The zoom adjusts to fit the selected node and all its visible descendants; the branch is centered in the viewport |
| Failed End Condition | User must manually adjust zoom and scroll to focus on a branch |
| Actors | Mind map author |
| Trigger | User right-clicks a node and selects "Zoom to Branch" |

---

## Improvement 18: Export Has No Preview Step

**Problem Statement:** All export operations (HTML, PDF, SVG, PNG, JPEG) are fire-and-forget with no preview. The PDF export dialog only asks for paper size and orientation, then generates the file. Users cannot see layout, margins, or clipping before committing.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > create a large map with 50+ nodes.
2. File > Export > Export as PDF.
3. Choose settings (paper size, orientation).
4. Observe: the PDF is generated immediately with no preview of layout, margins, or clipping.

**Use Cases under this Improvement:** UC-53, UC-54, UC-55

---

#### Use Case 53: Preview Export Layout Before Generating File

| Field | Description |
|---|---|
| Goal | See how the exported file will look before committing to the export |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open; user has opened an export dialog |
| Success End Condition | A preview pane shows a rendering of the export output with page boundaries, margins, and content layout; user can adjust settings and see the preview update |
| Failed End Condition | Export generates the file immediately with no preview (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens any Export dialog |

#### Use Case 54: Configure Export Margins and Scaling

| Field | Description |
|---|---|
| Goal | Control margins, scaling factor, and page distribution for exported documents |
| Scope & Level | System / User-goal |
| Preconditions | The export dialog is open (PDF, PNG, SVG, or image) |
| Success End Condition | User can set top/bottom/left/right margins, choose "fit to page" vs. "actual size", and control DPI for image exports |
| Failed End Condition | Export uses fixed, unconfigurable margins and scaling (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens export configuration options |

#### Use Case 55: Export Selected Branch Only

| Field | Description |
|---|---|
| Goal | Export only a specific subtree rather than the entire map |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected; an export dialog is open |
| Success End Condition | Only the selected branch and its descendants are included in the export; the root of the exported subtree becomes the visual root of the exported document |
| Failed End Condition | The entire map is always exported regardless of selection |
| Actors | Mind map author |
| Trigger | User selects a branch, then chooses File > Export and checks "Export selected branch only" |

---

## Improvement 19: No Keyboard Navigation Shortcut Discoverability

**Problem Statement:** FreeMind has 100+ keyboard shortcuts, many of which are completely undiscoverable from the UI. Examples: Space (toggle fold), E/D/S/F (navigate), F1-F9 (apply patterns), Alt+R/Alt+P (rich/plain text mode), Ctrl+Shift+B (cloud toggle). These are only documented in an external PDF/HTML file.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Create several child nodes.
3. Try to discover how to fold/unfold nodes — no tooltip, no menu shortcut indicator for Space.
4. Press E, D, S, F — node selection moves, but there is zero in-app indication these shortcuts exist.

**Use Cases under this Improvement:** UC-56, UC-57, UC-58

---

#### Use Case 56: In-App Keyboard Shortcut Cheat Sheet Overlay

| Field | Description |
|---|---|
| Goal | View all keyboard shortcuts grouped by category without leaving the application |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is running |
| Success End Condition | Pressing Shift+? or Help > Keyboard Shortcuts shows a translucent overlay listing all shortcuts organized by category (Navigation, Editing, Formatting, View, Tools) |
| Failed End Condition | User must open an external PDF/HTML file to learn shortcuts (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses Shift+? or selects Help > Keyboard Shortcuts |

#### Use Case 57: Show Shortcut Hints on Toolbar Button Tooltips

| Field | Description |
|---|---|
| Goal | Learn keyboard shortcuts through natural tooltip interaction |
| Scope & Level | System / Subfunction |
| Preconditions | Toolbar is visible |
| Success End Condition | Every toolbar button tooltip includes the keyboard shortcut (e.g., "Bold (Ctrl+B)"), not just the action name |
| Failed End Condition | Tooltips show only the action name without shortcut keys |
| Actors | Mind map author |
| Trigger | User hovers over a toolbar button |

#### Use Case 58: Searchable Command Palette for Action Discovery

| Field | Description |
|---|---|
| Goal | Find and execute any action by typing its name in a search palette |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is running with a map open |
| Success End Condition | Pressing Ctrl+Shift+P opens a command palette; typing filters available actions; each result shows the action name, description, and keyboard shortcut; pressing Enter executes the selected action |
| Failed End Condition | Users must navigate menus or know shortcuts by heart to find features (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses Ctrl+Shift+P |

---

## Improvement 20: Color Picker Has No Recent Colors or Project Palette

**Problem Statement:** The `JColorChooser` used for node color, edge color, cloud color, and background color is the standard Java color chooser with no "recent colors" section and no ability to save a project palette.

**How to Reproduce / Current State:**
1. Launch FreeMind > select a node > Format > Node Color.
2. Pick a custom color > close.
3. Select another node > Format > Node Color.
4. Observe: no "recent colors" list; must re-pick the same color manually.

**Use Cases under this Improvement:** UC-59, UC-60, UC-61

---

#### Use Case 59: Access Recent Colors in Color Picker

| Field | Description |
|---|---|
| Goal | Quickly reuse a color that was recently selected |
| Scope & Level | System / Subfunction |
| Preconditions | The user has previously selected at least one custom color in any color picker dialog |
| Success End Condition | The color picker shows a "Recent Colors" row displaying the last 10–20 colors selected; clicking a recent color selects it immediately |
| Failed End Condition | User must manually re-create colors each time (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens any color picker dialog (node color, edge color, cloud color, background color) |

#### Use Case 60: Save and Load a Project Color Palette

| Field | Description |
|---|---|
| Goal | Define a set of branded/project-specific colors and reuse them across maps |
| Scope & Level | System / User-goal |
| Preconditions | The color picker dialog is open |
| Success End Condition | User can save the current color to a named palette, load palettes from previous sessions, and share palette files with team members |
| Failed End Condition | No palette persistence; users must remember hex values (current behaviour) |
| Actors | Mind map author, Team member |
| Trigger | User clicks "Save to Palette" in the color picker |

#### Use Case 61: Apply Color Consistently Across a Branch

| Field | Description |
|---|---|
| Goal | Set the same color for a node and all its descendants in one action |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected |
| Success End Condition | User selects a color and checks "Apply to all descendants"; every node in the subtree receives the same color |
| Failed End Condition | User must individually color each node (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens Format > Node Color with a branch node selected and selects "Apply to descendants" |

---

## Improvement 21: Edge Formatting Must Be Applied Node by Node

**Problem Statement:** Edge style and width can only be set on the currently selected node(s). There is no "Apply to entire branch" or "Set as default for new nodes" option. The pattern system exists but is poorly discoverable.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Create a root node with 5 levels of children.
3. Select root > Format > Edge > Bezier.
4. Observe: only the root edge changes; all children retain the default edge style.

**Use Cases under this Improvement:** UC-62, UC-63, UC-64

---

#### Use Case 62: Apply Edge Style to Entire Branch Recursively

| Field | Description |
|---|---|
| Goal | Set a consistent edge style for a node and all its descendants |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected |
| Success End Condition | After selecting Format > Edge > [style] with "Apply to branch" checked, the selected edge style is applied to the selected node and all descendants recursively |
| Failed End Condition | Only the selected node's edge changes (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects an edge style with the "Apply to branch" option enabled |

#### Use Case 63: Set Default Edge Style for New Child Nodes

| Field | Description |
|---|---|
| Goal | Define the default edge style that new child nodes inherit when created |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open |
| Success End Condition | User sets a "default edge style" in preferences or via a map property; all newly created nodes inherit this style |
| Failed End Condition | New nodes always use the hardcoded default style regardless of user preference |
| Actors | Mind map author |
| Trigger | User changes the default edge style in Tools > Preferences or Format > Map Default Edge |

#### Use Case 64: Preview Edge Styles Before Applying

| Field | Description |
|---|---|
| Goal | See what each edge style looks like before committing to it |
| Scope & Level | System / Subfunction |
| Preconditions | The Format > Edge submenu is open |
| Success End Condition | Each edge style option (Linear, Bezier, Sharp Linear, Sharp Bezier) shows a small visual preview icon next to its name |
| Failed End Condition | Edge style names are text-only with no visual preview (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens Format > Edge submenu |

---

## Improvement 22: Node Drag Has No Alignment Guides or Snap Behaviour

**Problem Statement:** Node repositioning via drag-and-drop adjusts `hGap` and `shiftY` freely with no alignment guides, snap-to-grid, or visual indicators showing alignment with siblings. The only reset is double-clicking the drag handle.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New.
2. Add 5 sibling child nodes.
3. Drag one node vertically.
4. Observe: no alignment lines, no snap behaviour to align with siblings.

**Use Cases under this Improvement:** UC-65, UC-66, UC-67

---

#### Use Case 65: Snap Node Position to Sibling Alignment

| Field | Description |
|---|---|
| Goal | Automatically align a dragged node with its siblings when within a threshold |
| Scope & Level | System / Subfunction |
| Preconditions | A node is being dragged to reposition |
| Success End Condition | When the dragged node's edge aligns horizontally or vertically with a sibling within 5px, a guide line appears and the node snaps to alignment on release |
| Failed End Condition | Free-form positioning with no alignment assistance (current behaviour) |
| Actors | Mind map author |
| Trigger | User drags a node near an alignment boundary with a sibling |

#### Use Case 66: Show Visual Alignment Guides During Drag

| Field | Description |
|---|---|
| Goal | See alignment reference lines while repositioning nodes |
| Scope & Level | System / Subfunction |
| Preconditions | A node is being dragged |
| Success End Condition | Horizontal and vertical guide lines extend from sibling nodes showing potential alignment positions; lines appear and disappear dynamically as the node is moved |
| Failed End Condition | No visual feedback during drag (current behaviour) |
| Actors | Mind map author |
| Trigger | User begins dragging a node by its motion handle |

#### Use Case 67: Reset Node Position to Default via Context Menu

| Field | Description |
|---|---|
| Goal | Reset a manually positioned node back to its auto-layout position |
| Scope & Level | System / User-goal |
| Preconditions | A node has been manually repositioned (custom `hGap`/`shiftY` values) |
| Success End Condition | A context menu option "Reset Position" restores the node to its default auto-layout position; the operation is undoable |
| Failed End Condition | The only reset method is double-clicking the drag handle, which is undiscoverable (current behaviour) |
| Actors | Mind map author |
| Trigger | User right-clicks a repositioned node and selects "Reset Position" |

---

## Improvement 23: Print Preview Lacks Page Layout Controls

**Problem Statement:** The print preview dialog shows pages with basic forward/back navigation and zoom, but offers no control over margins, scale-to-fit, page break placement, or branch selection.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > create a wide map with 50+ nodes.
2. File > Print Preview.
3. Observe: multi-page layout with no control over content distribution.

**Use Cases under this Improvement:** UC-68, UC-69, UC-70

---

#### Use Case 68: Set Print Margins in Print Preview

| Field | Description |
|---|---|
| Goal | Adjust top, bottom, left, and right margins before printing |
| Scope & Level | System / User-goal |
| Preconditions | The print preview dialog is open |
| Success End Condition | Margin controls (spinner or slider) update the preview in real time; settings persist for the session |
| Failed End Condition | Margins are fixed and unconfigurable (current behaviour) |
| Actors | Mind map author |
| Trigger | User adjusts margin controls in the print preview dialog |

#### Use Case 69: Scale Map to Fit on N Pages

| Field | Description |
|---|---|
| Goal | Control how many pages the map spans when printed |
| Scope & Level | System / User-goal |
| Preconditions | The print preview dialog is open |
| Success End Condition | User can select "Fit to 1 page", "Fit to 2×2 pages", or "Actual size"; the preview updates accordingly |
| Failed End Condition | Map prints at a fixed scale that may span many pages uncontrollably (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects a scaling option in the print preview dialog |

#### Use Case 70: Print Selected Branch Only

| Field | Description |
|---|---|
| Goal | Print a specific subtree rather than the entire map |
| Scope & Level | System / User-goal |
| Preconditions | A node is selected before opening print preview |
| Success End Condition | Print preview shows only the selected branch; printing outputs only that subtree |
| Failed End Condition | The entire map is always printed regardless of selection (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects a node, then opens File > Print Preview |

---

## Improvement 24: Undo History Unbounded Memory Consumption

**Problem Statement:** While the undo stack has a configurable entry limit (default 100), each entry stores a full XML serialization of the action. For large maps with complex operations, undo entries can consume significant memory with no memory-based limit or warning before `OutOfMemoryError`.

**How to Reproduce / Current State:**
1. Launch FreeMind.
2. Open a very large `.mm` file (1000+ nodes).
3. Perform many paste operations of large branches.
4. Observe memory consumption growing with each undo entry.
5. Eventually, `OutOfMemoryError` occurs with no warning.

**Use Cases under this Improvement:** UC-71, UC-72, UC-73

---

#### Use Case 71: Cap Undo Memory Usage

| Field | Description |
|---|---|
| Goal | Prevent undo history from consuming more than a configurable portion of available memory |
| Scope & Level | System / Subfunction |
| Preconditions | The undo stack is accumulating entries from editing a large map |
| Success End Condition | Undo entries are evicted (oldest first) when total undo memory exceeds the configured limit (e.g., 50MB or 25% of heap); the operation is transparent to the user |
| Failed End Condition | Undo entries grow until `OutOfMemoryError` (current behaviour) |
| Actors | FreeMind application |
| Trigger | A new undo entry is pushed and total undo memory exceeds the threshold |

#### Use Case 72: Estimate and Display Undo Stack Memory Usage

| Field | Description |
|---|---|
| Goal | Show the user how much memory the undo stack is consuming |
| Scope & Level | System / Subfunction |
| Preconditions | A mind map is open with undo history |
| Success End Condition | A status bar or undo history panel shows approximate undo memory usage (e.g., "Undo: 45 entries, ~12MB") |
| Failed End Condition | User has no visibility into undo memory usage until crash occurs |
| Actors | Mind map author |
| Trigger | User opens the undo history panel or hovers over a memory indicator |

#### Use Case 73: Compact Undo Entries for Large Operations

| Field | Description |
|---|---|
| Goal | Reduce the memory footprint of undo entries for bulk operations (e.g., paste 500 nodes) |
| Scope & Level | System / Subfunction |
| Preconditions | A bulk operation generates an undo entry containing large XML content |
| Success End Condition | Undo entries for bulk operations are stored in compressed format or as incremental diffs rather than full XML snapshots |
| Failed End Condition | Every undo entry stores a full uncompressed XML copy of the affected nodes |
| Actors | FreeMind application |
| Trigger | An undo entry exceeding a size threshold (e.g., 1MB) is created |

---

## Improvement 25: Find & Replace and Report Bug Menu Items Are Non-Functional Placeholders

**Problem Statement:** The Edit menu contains "Find & Replace" and the Help menu contains "Report Bug" — both defined in `mindmap_menus.xml` but mapping to empty action placeholders. Additionally, the File > Export submenu contains placeholder entries for "Export as Text", "Export as Picture", and "Export as PDF" that do nothing, despite plugin-based equivalents existing.

**How to Reproduce / Current State:**
1. Launch FreeMind.
2. Edit > Find & Replace — nothing happens.
3. Help > Report Bug — nothing happens.
4. File > Export — some entries are placeholders mixed with functional ones.

**Use Cases under this Improvement:** UC-74, UC-75, UC-76

---

#### Use Case 74: Remove or Implement Find & Replace Menu Entry

| Field | Description |
|---|---|
| Goal | Ensure the "Find & Replace" menu item either works or is not present |
| Scope & Level | System / User-goal |
| Preconditions | The Edit menu is visible |
| Success End Condition | "Find & Replace" opens a functional dialog with search and replacement fields, case sensitivity toggle, and scope options (see Improvement 2) |
| Failed End Condition | The menu item exists but does nothing (current behaviour), confusing users |
| Actors | Mind map author |
| Trigger | User clicks Edit > Find & Replace |

#### Use Case 75: Report Bug Opens External Issue Tracker

| Field | Description |
|---|---|
| Goal | Quickly report a bug from within the application |
| Scope & Level | System / User-goal |
| Preconditions | Help > Report Bug menu item is visible |
| Success End Condition | Clicking "Report Bug" opens the project's issue tracker URL in the default browser, or shows a dialog with a link and pre-filled system information (OS, Java version, FreeMind version) |
| Failed End Condition | Menu item does nothing (current behaviour) |
| Actors | Mind map author |
| Trigger | User clicks Help > Report Bug |

#### Use Case 76: Remove Non-Functional Export Placeholder Entries

| Field | Description |
|---|---|
| Goal | Ensure all export menu entries either produce output or are hidden |
| Scope & Level | System / Subfunction |
| Preconditions | File > Export submenu is visible |
| Success End Condition | Placeholder entries are either removed from the menu or connected to the corresponding plugin-based export implementations (e.g., "Export as PDF" maps to the ExportPdf plugin) |
| Failed End Condition | Placeholder entries remain alongside functional ones, causing confusion about which exports work (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens File > Export |

---

## Improvement 26: No Accessibility Support for Screen Readers or High Contrast

**Problem Statement:** The codebase contains zero `AccessibleContext` implementations, no `setAccessibleName` calls, no screen reader support, no high-contrast mode, and no focus indicators beyond the default selection highlight. A grep for "accessibility", "a11y", "AccessibleContext", "setAccessibleName" returns zero results across the entire codebase.

**How to Reproduce / Current State:**
1. Launch FreeMind with a screen reader active (e.g., VoiceOver on macOS, NVDA on Windows).
2. Observe: the screen reader cannot identify nodes, menus have limited accessibility, and there is no way to navigate the mind map tree via accessible APIs.

**Use Cases under this Improvement:** UC-77, UC-78, UC-79

---

#### Use Case 77: Navigate Mind Map Nodes via Screen Reader

| Field | Description |
|---|---|
| Goal | Read and navigate mind map nodes using a screen reader |
| Scope & Level | System / User-goal |
| Preconditions | A screen reader is active; a mind map is open |
| Success End Condition | Each node announces its text, level, number of children, and position among siblings; arrow key navigation is accessible; fold state is announced |
| Failed End Condition | Screen reader cannot identify individual nodes or their relationships (current behaviour) |
| Actors | Mind map author using assistive technology |
| Trigger | User navigates the mind map with a screen reader active |

#### Use Case 78: High-Contrast Theme for Visual Accessibility

| Field | Description |
|---|---|
| Goal | Use FreeMind with a high-contrast color scheme suitable for low vision users |
| Scope & Level | System / User-goal |
| Preconditions | User has visual impairment requiring high-contrast display |
| Success End Condition | A "High Contrast" theme is available in preferences; it provides large text, high-contrast colors, thick borders, and clear focus indicators |
| Failed End Condition | All colors are hardcoded with no theme support; users with low vision struggle to distinguish UI elements (current behaviour) |
| Actors | Mind map author with visual impairment |
| Trigger | User enables the high-contrast theme in preferences |

#### Use Case 79: Keyboard-Only Complete Workflow

| Field | Description |
|---|---|
| Goal | Perform all mind map operations without using a mouse |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is running |
| Success End Condition | Every feature accessible via mouse is also accessible via keyboard: dialog navigation follows standard Tab/Shift+Tab conventions; all buttons are reachable; focus indicators are clearly visible |
| Failed End Condition | Some features (e.g., icon selection, color picking, node drag positioning) require mouse interaction with no keyboard alternative |
| Actors | Mind map author using keyboard only |
| Trigger | User interacts with FreeMind exclusively via keyboard |

---

## Improvement 27: No Dark Mode or Theme System

**Problem Statement:** FreeMind has hardcoded colors throughout (`#ffffff` background, `#000000` text, `#808080` edges) in `freemind.properties` with no theme abstraction. Every modern application supports dark mode, which reduces eye strain during extended use.

**How to Reproduce / Current State:**
1. Launch FreeMind.
2. Observe: white background, no option to switch to dark mode in any menu or preference.
3. Tools > Preferences — no theme or dark mode setting exists.

**Use Cases under this Improvement:** UC-80, UC-81, UC-82

---

#### Use Case 80: Enable Dark Mode Theme

| Field | Description |
|---|---|
| Goal | Switch the application to a dark color scheme |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is running |
| Success End Condition | Selecting View > Theme > Dark or a preferences toggle switches the UI to a dark colour scheme: dark background, light text, adjusted node colours, dark toolbars and menus |
| Failed End Condition | Only a white/light theme is available (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects the dark mode option in preferences or View menu |

#### Use Case 81: Follow System Theme Automatically

| Field | Description |
|---|---|
| Goal | Have FreeMind automatically match the OS light/dark mode setting |
| Scope & Level | System / Subfunction |
| Preconditions | The OS has a light/dark mode setting |
| Success End Condition | A "Follow System" option in preferences detects the OS theme and applies the corresponding FreeMind theme; changes take effect when the OS theme changes |
| Failed End Condition | FreeMind always uses the same theme regardless of OS setting |
| Actors | Mind map author, Operating System |
| Trigger | User selects "Follow System" in theme preferences, or the OS theme changes |

#### Use Case 82: Create and Import Custom Themes

| Field | Description |
|---|---|
| Goal | Define a custom color scheme and share it with others |
| Scope & Level | System / User-goal |
| Preconditions | The theme editor or theme file format is available |
| Success End Condition | User can create a custom theme defining all UI colors (background, text, edges, node fill, toolbar, menu); themes can be exported as files and imported by others |
| Failed End Condition | No theme customization beyond individual property edits |
| Actors | Mind map author, Theme designer |
| Trigger | User opens View > Theme > Create Custom Theme |

---

## Improvement 28: No Markdown Import or Export

**Problem Statement:** FreeMind supports HTML, XML, PDF, SVG, and image exports but has no Markdown support. Markdown is the dominant format for notes, documentation, and knowledge bases. The existing XSLT-based export system is architecturally ready for a Markdown template.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > Export.
2. Observe: no Markdown option in the export list.
3. File > Import — no Markdown import option exists.

**Use Cases under this Improvement:** UC-83, UC-84, UC-85

---

#### Use Case 83: Export Mind Map as Markdown Document

| Field | Description |
|---|---|
| Goal | Export the mind map as a hierarchical Markdown document using heading levels and nested lists |
| Scope & Level | System / User-goal |
| Preconditions | A mind map is open |
| Success End Condition | File > Export > Markdown produces a `.md` file where the root becomes an H1, level-1 children become H2, and deeper nodes become nested bullet lists or lower-level headings; node formatting (bold, italic) is preserved as Markdown syntax |
| Failed End Condition | No Markdown export option exists (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects File > Export > As Markdown |

#### Use Case 84: Import Markdown Document as Mind Map

| Field | Description |
|---|---|
| Goal | Create a mind map from an existing Markdown document's heading hierarchy |
| Scope & Level | System / User-goal |
| Preconditions | A `.md` file with headings and nested lists exists |
| Success End Condition | File > Import > Markdown creates a mind map where H1 becomes root, H2 becomes level-1 children, nested lists become deeper nodes; text formatting is preserved |
| Failed End Condition | No Markdown import option exists (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects File > Import > From Markdown and chooses a `.md` file |

#### Use Case 85: Copy Branch as Markdown to Clipboard

| Field | Description |
|---|---|
| Goal | Copy a selected branch as formatted Markdown text for pasting into other applications |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected |
| Success End Condition | Right-click > Copy as Markdown places Markdown-formatted text on the clipboard; pasting into a Markdown editor produces a valid document |
| Failed End Condition | Copy only produces FreeMind XML or plain text (current behaviour) |
| Actors | Mind map author |
| Trigger | User right-clicks a node and selects "Copy as Markdown" |

---

## Improvement 29: First-Time User Onboarding Is Non-Existent

**Problem Statement:** FreeMind opens to a blank map with no guidance. There is no tutorial, no getting-started wizard, and no tooltips introducing key interactions. The 15+ hidden keyboard shortcuts (Space, E/D/S/F, F1-F9, Alt+R) are completely undiscoverable.

**How to Reproduce / Current State:**
1. Install FreeMind and launch for the first time.
2. Observe: a blank map with "New Mindmap" as the root node. No tutorial, no hints, no "Did you know?" tips.

**Use Cases under this Improvement:** UC-86, UC-87, UC-88

---

#### Use Case 86: Interactive First-Run Tutorial

| Field | Description |
|---|---|
| Goal | Learn the fundamental interactions through a guided, interactive overlay |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is launched for the first time (or tutorial is manually triggered from Help menu) |
| Success End Condition | A step-by-step tutorial overlay guides the user through: adding a node (Insert/Enter), editing text (F2), folding branches (Space), navigating (arrow keys), and formatting (F1-F9 patterns) |
| Failed End Condition | User is presented with a blank map and must consult external documentation (current behaviour) |
| Actors | First-time user |
| Trigger | First launch of FreeMind (detected via absence of a config file) or Help > Tutorial |

#### Use Case 87: Contextual Tooltips for Key Actions

| Field | Description |
|---|---|
| Goal | Discover hidden features through context-sensitive hints |
| Scope & Level | System / Subfunction |
| Preconditions | User is performing an action that has a related hidden shortcut or feature |
| Success End Condition | A non-intrusive tooltip appears (e.g., when creating a node, show "Tip: Press Space to fold/unfold branches"); tips appear once and can be dismissed permanently |
| Failed End Condition | No contextual tips are shown (current behaviour) |
| Actors | Mind map author |
| Trigger | User performs an action with an associated tip for the first time |

#### Use Case 88: Sample Maps with Annotated Features

| Field | Description |
|---|---|
| Goal | Explore FreeMind features through example mind maps |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is installed |
| Success End Condition | Help > Example Maps opens a submenu of curated maps demonstrating features: "Keyboard Shortcuts", "Formatting Options", "Icons and Attributes", "Collaboration Setup" |
| Failed End Condition | No example maps are accessible from the application (current behaviour) |
| Actors | First-time user |
| Trigger | User selects Help > Example Maps |

---

## Improvement 30: Collaboration Features Require Manual Configuration with No Guided Setup

**Problem Statement:** The socket-based and Jabber collaboration features require manual configuration (server address, port, credentials) through form dialogs with no setup wizard, no auto-discovery, no test-connection button, and no connection status indicator in the main UI.

**How to Reproduce / Current State:**
1. Launch FreeMind > Tools > Collaboration.
2. Observe: form fields requiring server configuration with no wizard, no test button, and no status indicator.

**Use Cases under this Improvement:** UC-89, UC-90, UC-91

---

#### Use Case 89: Guided Collaboration Setup Wizard

| Field | Description |
|---|---|
| Goal | Set up a collaboration session through a step-by-step wizard |
| Scope & Level | System / User-goal |
| Preconditions | Two or more FreeMind instances are available on a network |
| Success End Condition | A wizard walks the user through: choosing collaboration type (host/join), entering connection details, testing the connection, and starting the session; progress and errors are clearly shown |
| Failed End Condition | User must manually fill form fields with no guidance (current behaviour) |
| Actors | Mind map author (host), Mind map author (participant) |
| Trigger | User selects Tools > Collaboration > Start Session |

#### Use Case 90: Test Connection Before Starting Collaboration

| Field | Description |
|---|---|
| Goal | Verify network connectivity to the collaboration server before committing |
| Scope & Level | System / Subfunction |
| Preconditions | The collaboration configuration dialog is open with server details entered |
| Success End Condition | A "Test Connection" button sends a ping to the server and shows the result: "Connection successful" or a specific error message |
| Failed End Condition | No way to test connectivity before starting a session (current behaviour) |
| Actors | Mind map author |
| Trigger | User clicks "Test Connection" in the collaboration dialog |

#### Use Case 91: Display Collaboration Status in Main UI

| Field | Description |
|---|---|
| Goal | See at a glance whether a collaboration session is active and who is connected |
| Scope & Level | System / User-goal |
| Preconditions | A collaboration session has been started |
| Success End Condition | A status bar indicator shows: connection state (connected/disconnected), number of participants, and a tooltip listing participant names |
| Failed End Condition | No visual indication of collaboration status in the main UI (current behaviour) |
| Actors | Mind map author, Collaborating participants |
| Trigger | Collaboration session state changes (connect, disconnect, participant join/leave) |

---

## Improvement 31: Filter System Provides No Live Preview of Matching Nodes

**Problem Statement:** The `FilterComposerDialog` allows building complex AND/OR filter conditions but provides no real-time preview of which nodes match. Users must compose, apply, observe, then re-edit — a laborious iterative loop.

**How to Reproduce / Current State:**
1. Launch FreeMind > create a map with mixed icons and text.
2. View > Show Filter Toolbar > click the filter compose button.
3. Build a condition.
4. Observe: no live preview of matching nodes until the filter is applied.

**Use Cases under this Improvement:** UC-92, UC-93, UC-94

---

#### Use Case 92: Live-Preview Matching Nodes During Filter Composition

| Field | Description |
|---|---|
| Goal | See which nodes match the filter conditions in real time while composing the filter |
| Scope & Level | System / User-goal |
| Preconditions | The filter composer dialog is open; a mind map is visible behind it |
| Success End Condition | As the user builds or modifies filter conditions, matching nodes are highlighted in the background map; a count label shows "N nodes match" |
| Failed End Condition | No feedback until the filter is applied (current behaviour) |
| Actors | Mind map author |
| Trigger | User adds, modifies, or removes a filter condition in the composer dialog |

#### Use Case 93: Save and Name Custom Filters for Reuse

| Field | Description |
|---|---|
| Goal | Save a complex filter configuration with a descriptive name for future use |
| Scope & Level | System / User-goal |
| Preconditions | The user has composed a filter with one or more conditions |
| Success End Condition | The filter is saved with a user-provided name and appears in a dropdown in the filter toolbar for one-click application |
| Failed End Condition | Filters must be rebuilt from scratch each time |
| Actors | Mind map author |
| Trigger | User clicks "Save Filter" in the filter composer dialog |

#### Use Case 94: Explain Filter Logic with Readable Summary

| Field | Description |
|---|---|
| Goal | Understand what a composed filter does in plain language |
| Scope & Level | System / Subfunction |
| Preconditions | A filter with one or more conditions exists |
| Success End Condition | A human-readable summary appears (e.g., "Show nodes where: text contains 'budget' AND icon is 'priority-1' OR text contains 'deadline'") |
| Failed End Condition | Filter logic is represented only as abstract condition widgets with no summary (current behaviour) |
| Actors | Mind map author |
| Trigger | User views a saved or in-progress filter |

---

## Improvement 32: Node Attributes (Key-Value Pairs) Are Completely Undocumented

**Problem Statement:** Nodes support key-value attributes accessible via Alt+F9, but there is no mention of attributes in the main menu, no tooltip, and no documentation. The feature only appears in the right-click context menu as "Edit Attributes." What attributes are for, how they interact with filtering, and their format are unexplained.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add a child node.
2. Press Alt+F9.
3. Observe: an attribute editor appears with no explanation.

**Use Cases under this Improvement:** UC-95, UC-96, UC-97

---

#### Use Case 95: Display Attribute Help Within the Attribute Editor

| Field | Description |
|---|---|
| Goal | Understand what attributes are and how to use them |
| Scope & Level | System / Subfunction |
| Preconditions | The attribute editor is open (via Alt+F9 or context menu) |
| Success End Condition | A help panel or tooltip within the dialog explains: what attributes are (key-value metadata), example uses (tagging, categorization, filtering), and how they interact with the filter system |
| Failed End Condition | The attribute editor has no help text (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens the attribute editor |

#### Use Case 96: Auto-Complete Attribute Keys from Previously Used Keys

| Field | Description |
|---|---|
| Goal | Maintain consistent attribute key names across nodes |
| Scope & Level | System / Subfunction |
| Preconditions | The attribute editor is open; other nodes in the map already have attributes |
| Success End Condition | When typing an attribute key, a dropdown suggests keys already used in other nodes (e.g., "Priority", "Status", "Owner") |
| Failed End Condition | Each attribute key must be typed from scratch with no consistency assistance |
| Actors | Mind map author |
| Trigger | User begins typing in the attribute key field |

#### Use Case 97: Display Node Attributes as Badges or Tags on the Node View

| Field | Description |
|---|---|
| Goal | See attribute values directly on nodes without opening the attribute editor |
| Scope & Level | System / User-goal |
| Preconditions | A node has one or more attributes assigned |
| Success End Condition | Key-value pairs are displayed as small badges or a compact table below the node text; display can be toggled via View > Show Attributes |
| Failed End Condition | Attributes are invisible on the map view; user must open the editor on each node to see them (current behaviour for some display modes) |
| Actors | Mind map author |
| Trigger | Attributes are assigned to a node and attribute display is enabled |

---

## Improvement 33: Rich Text vs Plain Text Mode Has No Visual Indicator

**Problem Statement:** Nodes can be in "Rich Formatting" (HTML) or "Plain Text" mode, toggled via Alt+R and Alt+P. There is no visual indicator on a node showing which mode it's in. Users may accidentally be in the wrong mode.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add a child node.
2. Press Alt+R to switch to rich text mode.
3. Observe: no visual change on the node. It is impossible to tell from the map view which mode a node is in.

**Use Cases under this Improvement:** UC-98, UC-99, UC-100

---

#### Use Case 98: Display Text Mode Indicator on Node

| Field | Description |
|---|---|
| Goal | Know at a glance whether a node is in rich text or plain text mode |
| Scope & Level | System / Subfunction |
| Preconditions | A mind map is open with nodes in different text modes |
| Success End Condition | Nodes in rich text mode display a small "HTML" badge or icon; plain text nodes show no badge (or a "TXT" badge if the user enables it) |
| Failed End Condition | No visual distinction between modes (current behaviour) |
| Actors | Mind map author |
| Trigger | User toggles a node's text mode or opens a map with mixed modes |

#### Use Case 99: Warn Before Switching from Rich Text to Plain Text

| Field | Description |
|---|---|
| Goal | Prevent accidental loss of formatting when switching a node from rich to plain text |
| Scope & Level | System / Subfunction |
| Preconditions | A node is in rich text mode with HTML formatting applied |
| Success End Condition | A confirmation dialog warns: "Switching to plain text will strip all formatting (bold, italic, links). Continue?" with OK/Cancel buttons |
| Failed End Condition | Mode switches silently and all formatting is lost without warning |
| Actors | Mind map author |
| Trigger | User presses Alt+P on a rich-text node |

#### Use Case 100: Batch Convert Nodes Between Text Modes

| Field | Description |
|---|---|
| Goal | Convert multiple selected nodes from plain to rich text (or vice versa) in one action |
| Scope & Level | System / User-goal |
| Preconditions | Multiple nodes are selected |
| Success End Condition | A menu action converts all selected nodes to the chosen mode; for rich-to-plain conversion, a confirmation warns about formatting loss |
| Failed End Condition | Each node must be individually toggled (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects multiple nodes and invokes Format > Convert to Plain Text / Rich Text |

---

## Improvement 34: Drag-and-Drop Modifier Keys Are Undocumented

**Problem Statement:** Drag-and-drop supports three operations (Move, Copy, Link) via modifier keys (Ctrl+drag changes vGap, middle-button for copy, right-click drag for link). These platform-specific modifier behaviours are never explained in the UI.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add child nodes.
2. Ctrl+drag the handle near a node.
3. Observe: all sibling spacing changes rather than the individual node moving — confusing with no explanation.

**Use Cases under this Improvement:** UC-101, UC-102, UC-103

---

#### Use Case 101: Show Drag Operation Mode in Cursor or Tooltip

| Field | Description |
|---|---|
| Goal | Know which drag operation (Move, Copy, Link) will occur based on modifier keys |
| Scope & Level | System / Subfunction |
| Preconditions | User is dragging a node |
| Success End Condition | The cursor changes to indicate the operation: standard cursor for Move, "+" cursor for Copy, chain-link cursor for Link; a small tooltip shows the operation name |
| Failed End Condition | Cursor does not clearly indicate the operation type (current behaviour provides limited cursor feedback) |
| Actors | Mind map author |
| Trigger | User begins dragging a node with or without modifier keys |

#### Use Case 102: Document Ctrl+Drag vGap Behaviour in Context

| Field | Description |
|---|---|
| Goal | Understand what Ctrl+drag does before accidentally modifying all sibling spacing |
| Scope & Level | System / Subfunction |
| Preconditions | User hovers over a node's drag handle |
| Success End Condition | A tooltip on the drag handle explains: "Drag to move node. Ctrl+drag to adjust sibling spacing. Double-click to reset position." |
| Failed End Condition | No tooltip exists; users accidentally change vGap when intending to copy (current behaviour) |
| Actors | Mind map author |
| Trigger | User hovers over a node's drag handle |

#### Use Case 103: Provide Drag-and-Drop Tutorial for First Use

| Field | Description |
|---|---|
| Goal | Learn drag-and-drop operations through an interactive guide |
| Scope & Level | System / User-goal |
| Preconditions | User initiates their first drag-and-drop operation |
| Success End Condition | A brief overlay or tooltip guide shows the three drag modes (Move, Copy via middle-click, Link via right-drag) with visual examples |
| Failed End Condition | User discovers modifier behaviours by accident or never discovers them |
| Actors | First-time user |
| Trigger | First drag-and-drop operation in a new FreeMind installation |

---

## Improvement 35: Arrow Link Creation Requires Undocumented Two-Node Selection

**Problem Statement:** Creating an arrow link requires selecting two nodes and pressing Ctrl+L, but the UI provides no guidance on how to multi-select (Ctrl+click) or what will happen. With only one node selected, the action may fail silently or create a self-referencing link.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add two child nodes.
2. Select one node only.
3. Press Ctrl+L.
4. Observe: confusing behaviour or error — no tooltip explains the two-node requirement.

**Use Cases under this Improvement:** UC-104, UC-105, UC-106

---

#### Use Case 104: Guided Arrow Link Creation Workflow

| Field | Description |
|---|---|
| Goal | Create an arrow link between two nodes with clear guidance |
| Scope & Level | System / User-goal |
| Preconditions | A mind map with at least two nodes is open |
| Success End Condition | Pressing Ctrl+L enters a "link mode" where the status bar says "Click a second node to create a link"; clicking a second node creates the arrow link; pressing Escape cancels |
| Failed End Condition | Ctrl+L with one node selected produces confusing results (current behaviour) |
| Actors | Mind map author |
| Trigger | User presses Ctrl+L or selects Insert > Arrow Link with one node selected |

#### Use Case 105: View All Links in a Side Panel

| Field | Description |
|---|---|
| Goal | See an overview of all arrow links and local links in the current map |
| Scope & Level | System / User-goal |
| Preconditions | A mind map contains at least one arrow link |
| Success End Condition | A side panel lists all links with source and target node names, link type, and colour; clicking a link scrolls to and highlights both connected nodes |
| Failed End Condition | User must visually scan the entire map to find arrow links (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens View > Link Overview Panel |

#### Use Case 106: Delete Arrow Link via Context Menu

| Field | Description |
|---|---|
| Goal | Remove an arrow link without knowing the keyboard shortcut |
| Scope & Level | System / User-goal |
| Preconditions | An arrow link exists between two nodes |
| Success End Condition | Right-clicking either end node shows a submenu of connected links; selecting one offers "Delete Link"; the deletion is undoable |
| Failed End Condition | Link deletion requires finding the right menu path or knowing the internal link ID |
| Actors | Mind map author |
| Trigger | User right-clicks a node with arrow links and selects "Delete Link" |

---

## Improvement 36: Node Style Names Are Not Previewed or Explained

**Problem Statement:** The Format > Style submenu offers "Fork" and "Bubble" styles but does not explain or preview what each looks like. Additional styles ("Combined", "As Parent") are defined in properties but may not appear in menus. There is no tooltip or visual preview.

**How to Reproduce / Current State:**
1. Launch FreeMind > File > New > add a node.
2. Right-click > Format > Style.
3. Observe: "Fork" and "Bubble" options with no preview or description.

**Use Cases under this Improvement:** UC-107, UC-108, UC-109

---

#### Use Case 107: Preview Node Style Before Applying

| Field | Description |
|---|---|
| Goal | See what a node style looks like before selecting it |
| Scope & Level | System / Subfunction |
| Preconditions | The Format > Style submenu is open |
| Success End Condition | Each style option shows a small icon preview of the style (Fork = simple line, Bubble = rounded rectangle); hovering previews the style on the selected node |
| Failed End Condition | Text-only labels with no visual indication (current behaviour) |
| Actors | Mind map author |
| Trigger | User opens Format > Style submenu |

#### Use Case 108: Apply Style to Branch Recursively

| Field | Description |
|---|---|
| Goal | Set a consistent node style for a node and all its descendants |
| Scope & Level | System / User-goal |
| Preconditions | A node with children is selected |
| Success End Condition | Format > Style > [style] with "Apply to branch" sets the chosen style on the selected node and all descendants |
| Failed End Condition | Style must be applied node by node (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects a style with "Apply to branch" enabled |

#### Use Case 109: Explain "As Parent" and "Combined" Styles in UI

| Field | Description |
|---|---|
| Goal | Understand what non-obvious style options do |
| Scope & Level | System / Subfunction |
| Preconditions | The style selection UI is visible |
| Success End Condition | Tooltips or a help icon explain: "As Parent" inherits the parent node's style; "Combined" uses Fork for root-level and Bubble for deeper nodes |
| Failed End Condition | These styles exist in properties but may not appear in menus, and have no explanation when they do |
| Actors | Mind map author |
| Trigger | User hovers over a style option |

---

## Improvement 37: Scripting Engine Has No In-App Documentation or Syntax Highlighting

**Problem Statement:** The scripting system (Groovy/BeanShell) provides a bare text area editor (`ScriptEditorPanel`) with no syntax highlighting, no API reference, no example scripts, and no help button.

**How to Reproduce / Current State:**
1. Launch FreeMind > Tools > Scripting > Edit Script.
2. Observe: a blank text area with no syntax highlighting, no API documentation, and no examples.

**Use Cases under this Improvement:** UC-110, UC-111, UC-112

---

#### Use Case 110: Syntax-Highlighted Script Editor

| Field | Description |
|---|---|
| Goal | Edit scripts with syntax highlighting for readability |
| Scope & Level | System / User-goal |
| Preconditions | The script editor is open |
| Success End Condition | Keywords, strings, comments, and API methods are highlighted in distinct colors; bracket matching is supported |
| Failed End Condition | All text is in a single font and color (current behaviour) |
| Actors | Mind map author, Developer |
| Trigger | User opens Tools > Scripting > Edit Script |

#### Use Case 111: In-Editor API Reference and Auto-Complete

| Field | Description |
|---|---|
| Goal | Discover available scripting API methods without consulting external documentation |
| Scope & Level | System / User-goal |
| Preconditions | The script editor is open |
| Success End Condition | Typing `node.` triggers a dropdown of available methods with descriptions; a help panel shows the full API reference |
| Failed End Condition | No API documentation is available in-app (current behaviour) |
| Actors | Mind map author, Developer |
| Trigger | User types an object name followed by `.` in the script editor |

#### Use Case 112: Run Script with Error Reporting

| Field | Description |
|---|---|
| Goal | Execute a script and see results or errors inline |
| Scope & Level | System / User-goal |
| Preconditions | A script is entered in the editor |
| Success End Condition | Clicking "Run" executes the script; output is displayed in a results panel below the editor; errors show the line number and a clear message |
| Failed End Condition | Script errors may be silently swallowed or shown as raw Java exceptions |
| Actors | Mind map author, Developer |
| Trigger | User clicks "Run Script" or presses a keyboard shortcut |

---

## Improvement 38: Hardcoded Strings Throughout Codebase Bypass Internationalization

**Problem Statement:** While FreeMind has extensive i18n resource bundles for 15+ languages, numerous user-facing strings are hardcoded in Java source files rather than loaded from resource bundles. Examples include error messages written directly to `System.err`, dialog titles using raw strings, and status messages that cannot be translated.

**How to Reproduce / Current State:**
1. Set FreeMind's language to a non-English locale (e.g., German, Japanese).
2. Trigger an error condition (e.g., save to a read-only path).
3. Observe: some error messages appear in English because they are hardcoded (e.g., `MindMapMapModel.java` line 265: `System.err.println("Attempt to save read-only map.")`).

**Use Cases under this Improvement:** UC-113, UC-114, UC-115

---

#### Use Case 113: Externalize All User-Facing Strings to Resource Bundles

| Field | Description |
|---|---|
| Goal | Ensure every string visible to the user is loaded from a localizable resource bundle |
| Scope & Level | System / Subfunction |
| Preconditions | The codebase contains hardcoded English strings in Java source files |
| Success End Condition | All user-facing strings (error messages, dialog titles, status bar text, tooltips) are replaced with `getText("key")` calls referencing resource bundles; no raw English strings remain in UI-facing code |
| Failed End Condition | Mixed-language UI when running in a non-English locale |
| Actors | Developer, Translator |
| Trigger | Developer identifies and externalizes hardcoded strings |

#### Use Case 114: Display Error Messages in User's Configured Language

| Field | Description |
|---|---|
| Goal | See error messages in the language selected in preferences |
| Scope & Level | System / User-goal |
| Preconditions | FreeMind is configured with a non-English locale; an error condition occurs |
| Success End Condition | The error dialog shows the message in the configured language |
| Failed End Condition | Error appears in English despite the UI being in another language |
| Actors | Mind map author (non-English speaking) |
| Trigger | An error condition occurs |

#### Use Case 115: Validate Translation Completeness at Build Time

| Field | Description |
|---|---|
| Goal | Ensure every resource key used in code has a translation in each supported locale |
| Scope & Level | System / Subfunction |
| Preconditions | Resource bundles exist for multiple languages |
| Success End Condition | A build-time check compares keys across all resource bundles and reports missing translations; missing keys are logged as warnings |
| Failed End Condition | Missing translations cause blank labels or fall back to keys at runtime with no advance warning |
| Actors | Developer, Build system |
| Trigger | Build process runs |

---

## Improvement 39: Presentation / Slideshow Mode Does Not Exist

**Problem Statement:** FreeMind has no presentation mode that steps through branches sequentially, despite having the building blocks (fold/unfold, zoom) to support one. Users who want to present from their mind maps must manually fold/unfold and zoom during the presentation.

**How to Reproduce / Current State:**
1. Launch FreeMind > create a structured map.
2. Attempt to present it — no presentation mode exists.
3. User must manually Space-fold branches and zoom during a live presentation.

**Use Cases under this Improvement:** UC-116, UC-117, UC-118

---

#### Use Case 116: Start Presentation Mode from Current Node

| Field | Description |
|---|---|
| Goal | Begin a full-screen presentation starting from the selected node |
| Scope & Level | System / User-goal |
| Preconditions | A mind map with a multi-level hierarchy is open |
| Success End Condition | The application enters full-screen mode; the selected node is centered and zoomed; pressing Right arrow or Space advances to the next branch (depth-first); pressing Left goes back; pressing Escape exits presentation mode |
| Failed End Condition | No presentation mode exists (current behaviour) |
| Actors | Mind map author (presenter) |
| Trigger | User selects View > Presentation Mode or presses F5 |

#### Use Case 117: Configure Presentation Traversal Order

| Field | Description |
|---|---|
| Goal | Choose how the presentation traverses the map (depth-first, breadth-first, or custom order) |
| Scope & Level | System / User-goal |
| Preconditions | Presentation mode is being configured |
| Success End Condition | A settings dialog allows choosing traversal order; nodes can be reordered manually for custom sequences |
| Failed End Condition | Traversal order is fixed and may not match the presenter's narrative flow |
| Actors | Mind map author |
| Trigger | User opens presentation settings before starting |

#### Use Case 118: Presentation Mode with Progressive Reveal

| Field | Description |
|---|---|
| Goal | Reveal child nodes one at a time during a presentation |
| Scope & Level | System / User-goal |
| Preconditions | Presentation mode is active on a node with children |
| Success End Condition | Each press of Space or Right arrow reveals the next child node while keeping previously revealed nodes visible; the map auto-zooms to fit revealed content |
| Failed End Condition | All children are shown at once when unfolding a branch |
| Actors | Mind map author (presenter), Audience |
| Trigger | User advances the presentation |

---

## Improvement 40: Version History / Map Diffing Not Available

**Problem Statement:** The auto-save mechanism creates timestamped backup files (`FM_<mapname>_<timestamp>.mm`) but there is no UI to browse, compare, or restore from them. Users must manually navigate the temp directory.

**How to Reproduce / Current State:**
1. Launch FreeMind > create a map > edit extensively > save.
2. Auto-save creates backup files in the temp directory.
3. There is no way to view these backups from within FreeMind.

**Use Cases under this Improvement:** UC-119, UC-120, UC-121

---

#### Use Case 119: Browse Version History of Current Map

| Field | Description |
|---|---|
| Goal | View a list of auto-save and manual-save versions of the current map |
| Scope & Level | System / User-goal |
| Preconditions | At least one auto-save backup exists |
| Success End Condition | A dialog lists all available versions with timestamps, file sizes, and a "Preview" button; clicking a version shows it in a read-only preview panel |
| Failed End Condition | User must manually browse the temp directory (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects File > Version History |

#### Use Case 120: Visual Diff Between Map Versions

| Field | Description |
|---|---|
| Goal | See what changed between two versions of a map |
| Scope & Level | System / User-goal |
| Preconditions | Two or more versions of the map are available |
| Success End Condition | A side-by-side or overlay view highlights: added nodes (green), deleted nodes (red), modified nodes (yellow), moved nodes (blue) |
| Failed End Condition | No comparison tool exists; user must manually compare XML files |
| Actors | Mind map author |
| Trigger | User selects two versions in the version history dialog and clicks "Compare" |

#### Use Case 121: Restore a Previous Map Version

| Field | Description |
|---|---|
| Goal | Revert the current map to a previous auto-saved or manually saved state |
| Scope & Level | System / User-goal |
| Preconditions | A previous version exists in the version history |
| Success End Condition | The selected version replaces the current map; the current state is backed up before restoration; the operation is undoable |
| Failed End Condition | User must manually copy backup files from the temp directory and rename them |
| Actors | Mind map author |
| Trigger | User selects a version and clicks "Restore" in the version history dialog |

---

## Improvement 41: Node Templates / Quick-Insert Library Does Not Exist

**Problem Statement:** The pattern system (`ManagePatternsPopupDialog`) allows saving formatting styles but not node content templates. There is no template library for pre-structured subtrees (e.g., "Meeting Notes", "SWOT Analysis").

**How to Reproduce / Current State:**
1. Launch FreeMind > create a "Meeting Notes" structure manually: root > Date, Attendees, Agenda, Action Items.
2. Next meeting, must recreate the same structure manually — no way to save it as a template.

**Use Cases under this Improvement:** UC-122, UC-123, UC-124

---

#### Use Case 122: Save a Branch as a Reusable Template

| Field | Description |
|---|---|
| Goal | Save the structure and content of a branch as a named template |
| Scope & Level | System / User-goal |
| Preconditions | A node with children exists representing a useful structure |
| Success End Condition | User right-clicks > "Save as Template", provides a name; the branch structure (with placeholder text) is saved to the template library |
| Failed End Condition | User must manually recreate structures each time (current behaviour) |
| Actors | Mind map author |
| Trigger | User right-clicks a branch root and selects "Save as Template" |

#### Use Case 123: Insert a Template as a Subtree

| Field | Description |
|---|---|
| Goal | Insert a pre-defined template structure under the selected node |
| Scope & Level | System / User-goal |
| Preconditions | A node is selected; at least one template exists in the library |
| Success End Condition | Insert > Template > [template name] creates the template subtree as children of the selected node; placeholder text is editable |
| Failed End Condition | No template insertion feature exists (current behaviour) |
| Actors | Mind map author |
| Trigger | User selects Insert > Template |

#### Use Case 124: Share Templates with Team Members

| Field | Description |
|---|---|
| Goal | Export templates for use by other FreeMind users |
| Scope & Level | System / User-goal |
| Preconditions | One or more templates exist in the user's template library |
| Success End Condition | Templates can be exported as `.mmtemplate` files and imported by others; imported templates appear in the Insert > Template menu |
| Failed End Condition | Templates are not shareable (they only exist locally) |
| Actors | Mind map author, Team member |
| Trigger | User selects File > Export Template or File > Import Template |

---

## Appendix: Improvement-to-Use-Case Traceability Matrix

| Improvement | Use Cases | Count |
|---|---|---|
| 1. Inline Node Editor No Wrap | UC-1, UC-2, UC-3 | 3 |
| 2. Find Lacks Replace | UC-4, UC-5, UC-6 | 3 |
| 3. Icon Selection No Search | UC-7, UC-8, UC-9 | 3 |
| 4. Broken DES Encryption | UC-10, UC-11, UC-12, UC-13 | 4 |
| 5. File Save Resource Leak | UC-14, UC-15, UC-16 | 3 |
| 6. File Lock Reader Leak | UC-17, UC-18, UC-19 | 3 |
| 7. JoinNodes HTML Corruption | UC-20, UC-21, UC-22 | 3 |
| 8. EditServer Socket Leak | UC-23, UC-24, UC-25 | 3 |
| 9. Silent Auto-Save Failures | UC-26, UC-27, UC-28 | 3 |
| 10. Empty Catch Blocks | UC-29, UC-30, UC-31 | 3 |
| 11. Undo/Redo No Visual History | UC-32, UC-33, UC-34 | 3 |
| 12. Underline Non-Functional | UC-35, UC-36, UC-37 | 3 |
| 13. No Visual Tab Bar | UC-38, UC-39, UC-40 | 3 |
| 14. Clipboard Owner Null | UC-41, UC-42, UC-43 | 3 |
| 15. Paste NullPointerException | UC-44, UC-45, UC-46 | 3 |
| 16. Preferences Overwhelming | UC-47, UC-48, UC-49 | 3 |
| 17. Zoom No Custom Percentage | UC-50, UC-51, UC-52 | 3 |
| 18. Export No Preview | UC-53, UC-54, UC-55 | 3 |
| 19. Shortcut Undiscoverability | UC-56, UC-57, UC-58 | 3 |
| 20. Color Picker No Recent Colors | UC-59, UC-60, UC-61 | 3 |
| 21. Edge Format Node-by-Node | UC-62, UC-63, UC-64 | 3 |
| 22. Node Drag No Alignment | UC-65, UC-66, UC-67 | 3 |
| 23. Print Preview No Layout Control | UC-68, UC-69, UC-70 | 3 |
| 24. Undo Memory Unbounded | UC-71, UC-72, UC-73 | 3 |
| 25. Placeholder Menu Items | UC-74, UC-75, UC-76 | 3 |
| 26. No Accessibility Support | UC-77, UC-78, UC-79 | 3 |
| 27. No Dark Mode | UC-80, UC-81, UC-82 | 3 |
| 28. No Markdown Support | UC-83, UC-84, UC-85 | 3 |
| 29. No Onboarding | UC-86, UC-87, UC-88 | 3 |
| 30. Collaboration No Guided Setup | UC-89, UC-90, UC-91 | 3 |
| 31. Filter No Live Preview | UC-92, UC-93, UC-94 | 3 |
| 32. Undocumented Attributes | UC-95, UC-96, UC-97 | 3 |
| 33. No Text Mode Indicator | UC-98, UC-99, UC-100 | 3 |
| 34. Drag Modifiers Undocumented | UC-101, UC-102, UC-103 | 3 |
| 35. Arrow Link Undocumented | UC-104, UC-105, UC-106 | 3 |
| 36. Node Style No Preview | UC-107, UC-108, UC-109 | 3 |
| 37. Scripting No Documentation | UC-110, UC-111, UC-112 | 3 |
| 38. Hardcoded i18n Strings | UC-113, UC-114, UC-115 | 3 |
| 39. No Presentation Mode | UC-116, UC-117, UC-118 | 3 |
| 40. No Version History | UC-119, UC-120, UC-121 | 3 |
| 41. No Node Templates | UC-122, UC-123, UC-124 | 3 |
| **TOTAL** | | **127** |
