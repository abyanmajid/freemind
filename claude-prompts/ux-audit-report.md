Ran on Claude Opus 4.6

```
You are a senior UX auditor and software QA engineer. Your task is to thoroughly audit Freemind — a Java-based mind mapping desktop application.

Phase 1 — Exploration (do this first, silently)
Launch the application fresh (no pre-existing mind maps). Systematically explore every menu, toolbar button, keyboard shortcut, dialog box, right-click context menu, and node interaction. Create at least one mind map from scratch during this process, adding nodes, child nodes, links, icons, notes, and formatting. Try importing/exporting, undo/redo, folding/unfolding nodes, and navigating via keyboard. Take note of anything confusing, broken, missing, or undocumented.

Phase 2 — Report
Answer each of the following four questions. For each question, provide a long, thorough, numbered list (aim for 10+ items per category). Each item must:
- Name the specific feature/function
- Describe the problem or opportunity in 1–2 crisp sentences
- Give a step-by-step GUI reproduction path starting from a cold app launch with no existing maps (e.g. "File → New → right-click root node → Insert Child Node → press F2 to rename → type a long string of 200+ characters → observe that the node box does not resize and text overflows")

Q1. Which features/functions have room for improvement?
List UX friction points, inefficient workflows, and design choices that work but could be meaningfully better.

Q2. Which features/functions have defects?
List bugs, crashes, visual glitches, unexpected behaviours, or anything that doesn't work as a reasonable user would expect.

Q3. Which features/functions lack instructions or documentation?
List anything that is non-obvious, has no tooltip/help text, is undocumented in the UI, or would confuse a first-time user with no prior Freemind experience.

Q4. What new features/functions could improve the user experience?
List additions that would modernise the app or address clear gaps — describe what the feature would do and why a user would want it, grounded in something you observed while exploring.

Do not summarise or truncate. Prioritise depth and specificity over brevity.nice. now can u optimise the use case prompt one to also tell it that its an expert in something, all that jazz9:57 PMYou are a senior business analyst and software architect with deep expertise in requirements engineering, UX design patterns, and object-oriented systems. You have extensive experience writing Use Case Requirements documents for desktop applications and enterprise software.

Your task is to analyze this codebase thoroughly and generate a comprehensive Use Case Requirements document.

Identify as many meaningful use cases as possible — focus on use cases that represent real, significant interactions or goals a user or system actor would care about. Include both primary happy-path use cases and important alternative/failure scenarios. Avoid trivial or redundant nitpicks, but do include genuinely small-but-meaningful use cases if they represent a distinct user goal.

For each use case, structure it exactly as follows:

Use Case [N]: [Name]

| Field | Description |
|---|---|
| Goal | What the actor is trying to achieve |
| Scope & Level | The system boundary and level (e.g., System/User-goal, Organization/Summary, Subfunction) |
| Preconditions | What must be true before this use case begins |
| Success End Condition | What the world looks like when the use case succeeds |
| Failed End Condition | What the world looks like if the use case fails |
| Actors | Primary and supporting actors involved |
| Trigger | What initiates this use case |

Before writing any use cases, silently explore the entire codebase — scan all source files, modules, classes, features, and configuration. Build a mental model of every capability the system exposes to the user. Then write your use cases from that complete picture.

Group related use cases under clear section headings. Aim for completeness and depth — 30+ well-reasoned use cases is the target. Prioritise specificity over brevity; each field should be concrete and grounded in what the system actually does, not vague generalisations.

Once complete, save the full document as a Markdown file at claude-prompts/use-case-requirements.md, creating the directory if it doesn't exist.
```

# FreeMind UX Audit Report

---

## Q1. Features/Functions with Room for Improvement

1. **Node Inline Editing — No Text Wrapping or Resize Feedback**
   The inline text editor (EditNodeTextField) uses a fixed-width JTextField with a minimum width of 150px for leaf nodes or 50px for others. When typing long text, the field does not dynamically grow or wrap — users must switch to the long-node editor (Alt+Enter) for multi-line content, but there is no visual cue that this alternative exists.
   _Reproduction:_ Launch FreeMind → File → New → right-click root node → Insert Child Node → press F2 → type a string of 200+ characters → observe the text field scrolls horizontally but does not wrap or visually indicate overflow.

2. **Find/Search — No Replace Functionality**
   The Find dialog (Ctrl+F) supports only forward text search with a single "Find in notes too" checkbox. There is no Find & Replace capability despite a placeholder menu entry existing in the XML configuration. Users who want to rename a term across many nodes must manually edit each one.
   _Reproduction:_ Launch FreeMind → File → New → create 10 child nodes each containing the word "draft" → Edit → Find & Replace → observe the menu item exists but does nothing (placeholder with empty action).

3. **Icon Selection — No Search or Filtering**
   The icon selection popup (IconSelectionPopupDialog) displays all 100+ icons in a flat grid with no search box, no categories, and no favorites. Finding a specific icon requires scanning the entire grid visually each time. The description label at the bottom only shows the icon name when hovering — there is no way to type "priority" to jump to priority icons.
   _Reproduction:_ Launch FreeMind → File → New → select root node → Insert → Icons → observe a large grid of icons with no search field, no category tabs, and no way to filter.

4. **Preferences Dialog — Overwhelming Flat List**
   The Options/Preferences panel (OptionPanel.java) presents dozens of configuration options across categories in a split-pane layout. Categories are listed in a left panel but the right panel can contain 30+ options per category with no visual grouping, collapsible sections, or "search settings" functionality. Tooltip text exists in the resource bundle but many options have cryptic property-key-based labels (e.g., "el\_\_enter_confirms_by_default").
   _Reproduction:_ Launch FreeMind → Tools → Preferences → navigate to Appearance tab → observe a long scrollable list of options with no visual hierarchy, no grouping, and some labels that are internal property keys.

5. **Edge Formatting — Must Format Each Node Individually**
   Edge style and width can only be set on the currently selected node(s). There is no "Apply to entire branch" or "Set as default for new nodes" option in the UI. Users creating consistent-looking maps must select every node individually or use patterns, but the pattern system itself is poorly discoverable.
   _Reproduction:_ Launch FreeMind → File → New → create a root node with 5 levels of children → select root → Format → Edge → Bezier → observe only the root edge changes, children retain default style.

6. **Zoom Controls — No Direct Percentage Entry**
   The zoom combobox in the toolbar offers fixed levels (50%, 75%, 100%, 125%, 150%, 200%) plus a single "user defined" option. There is no way to type an arbitrary zoom level like 110% or 175% directly. The zoom in/out shortcuts (Alt+Up/Down) step through the fixed list rather than allowing continuous zoom.
   _Reproduction:_ Launch FreeMind → observe the zoom dropdown in the toolbar → try typing "110" → observe no way to enter a custom value directly in the dropdown.

7. **Undo/Redo — No Visual History or Multi-Step Preview**
   Undo (Ctrl+Z) and Redo (Ctrl+Y) operate as a simple stack with no visual history list. Users cannot see what action they are about to undo or jump to a specific past state. The undo stack silently truncates at 100 entries (configurable but hidden in properties) with no warning when approaching the limit.
   _Reproduction:_ Launch FreeMind → File → New → perform 15 distinct edits (add nodes, rename, format) → press Ctrl+Z repeatedly → observe no indicator showing what action was undone, no history panel, no way to jump to a specific state.

8. **Export Workflow — No Live Preview**
   All export operations (HTML, PDF, SVG, PNG, JPEG) are fire-and-forget with no preview step. The PDF export dialog only asks for paper size and orientation, then generates the file. Users cannot see how their map will look before committing to the export, leading to trial-and-error iteration.
   _Reproduction:_ Launch FreeMind → File → New → create a large map → File → Export → Export as PDF → choose settings → observe the PDF is generated with no preview of layout, margins, or clipping.

9. **Tab/Map Management — No Visual Tab Bar**
   Multiple open maps are managed via the Maps menu with radio button selection. There is no visual tab bar, no drag-to-reorder, and no close button per tab. Switching between maps requires opening a menu each time rather than clicking a visible tab.
   _Reproduction:_ Launch FreeMind → File → Open → open 3 different .mm files → observe the Maps menu shows radio buttons for each file, but there is no persistent visual tab bar at the top.

10. **Node Drag — No Snap-to-Grid or Alignment Guides**
    Node repositioning via drag-and-drop adjusts hGap and shiftY parameters freely with no alignment guides, no snap-to-grid, and no visual indicators showing alignment with sibling nodes. The only way to reset positioning is to double-click the drag handle, which resets to defaults — there is no fine-grained alignment tool.
    _Reproduction:_ Launch FreeMind → File → New → add 5 sibling child nodes → drag one node vertically → observe no alignment lines or snap behavior to help align it with siblings.

11. **Color Picker — No Recent Colors or Saved Palette**
    The JColorChooser used for node color, edge color, cloud color, and background color is the standard Java color chooser with no "recent colors" section and no ability to save a project palette. Users who want to maintain a consistent color scheme must remember hex values or pick colors each time.
    _Reproduction:_ Launch FreeMind → select a node → Format → Node Color → pick a custom color → close → select another node → Format → Node Color → observe no "recent colors" list; must re-pick the same color manually.

12. **Print Preview — No Page Layout Control**
    The print preview dialog shows pages with basic forward/back navigation and zoom, but offers no control over margins, scaling to fit, page break placement, or which branches to include. For large maps, the preview may span many pages with no control over how content is distributed.
    _Reproduction:_ Launch FreeMind → File → New → create a wide map with 50+ nodes → File → Print Preview → observe multi-page layout with no control over how content distributes across pages.

13. **Collaboration — Complex Setup with No Guided Workflow**
    The socket-based and Jabber collaboration features require manual configuration (server address, port, credentials) through form dialogs with no setup wizard, no auto-discovery, and no indication of connection status in the main UI. The Jabber port is hardcoded and not configurable without recompilation.
    _Reproduction:_ Launch FreeMind → Tools → Collaboration → observe form fields requiring server configuration with no wizard, no test-connection button, and no status indicator.

14. **Paste Operation — No Paste-Special Options**
    While "Paste as Plain Text" exists (Ctrl+Shift+V), there is no "Paste Special" dialog that would let users choose from available clipboard formats (nodes, HTML, RTF, image, text). Users must guess which paste variant to use.
    _Reproduction:_ Launch FreeMind → copy rich content from a web browser → Ctrl+V pastes formatted content → no way to choose between available formats without using the separate Ctrl+Shift+V shortcut.

15. **Filter System — Complex Boolean Builder with No Visual Feedback**
    The FilterComposerDialog allows building complex AND/OR filter conditions but provides no real-time preview of which nodes match. Users must compose the filter, apply it, observe results, then re-edit if wrong — a laborious loop for complex filters.
    _Reproduction:_ Launch FreeMind → create a map with mixed icons and text → View → Show Filter Toolbar → click filter compose button → build a condition → observe no live preview of matching nodes until the filter is applied.

---

## Q2. Features/Functions with Defects

1. **Empty Catch Blocks Silently Swallow Errors Throughout Codebase**
   77+ empty catch blocks exist across the codebase. Critical examples include: `EncryptNode.java` line 186 where node creation failure during encryption is silently ignored; `EditServer.java` lines 150-161 where socket cleanup is entirely commented out in the finally block causing connection leaks; `MindMapMapModel.java` where file lock `NumberFormatException` handling is commented out, causing crashes on corrupt semaphore files.
   _Reproduction:_ Launch FreeMind → create a node → right-click → Encrypt → enter a password → if the underlying encryption operation fails (e.g., due to missing crypto provider), the node appears encrypted but content may be lost — no error is shown.

2. **Paste with No Node Selected Causes NullPointerException**
   `PasteAction.actionPerformed()` calls `getSelected()` and passes the result directly to `paste()` with no null check. If the user somehow triggers paste with no node selected (e.g., via keyboard shortcut immediately after closing a map), a NullPointerException occurs.
   _Reproduction:_ Launch FreeMind → File → New → click on empty canvas (deselecting all nodes) → Ctrl+V → potential NullPointerException crash.

3. **File Save Resource Leak**
   In `MindMapMapModel.saveInternal()`, a `FileOutputStream` wrapped in `BufferedWriter` is created without try-with-resources. If `getXml()` throws an exception during serialization, the output stream is never closed, leaking a file descriptor. Repeated save failures can exhaust file handles.
   _Reproduction:_ Launch FreeMind → create a map → save to a path → make the file read-only externally → attempt Save → error dialog appears but file handle may leak → repeat 1000+ times to exhaust descriptors (theoretical).

4. **File Lock Reader Never Closed on Normal Path**
   In `MindMapMapModel.LockManager.tryToLock()`, the `BufferedReader` reading the semaphore file is closed on one branch (stale lock) but never closed on the normal return path (line 463: `return lockingUser`). This leaks file descriptors and on Windows prevents the semaphore file from being deleted later.
   _Reproduction:_ Launch FreeMind → open a .mm file that is already locked by another user → observe lock detection works → close and reopen repeatedly → on Windows, the lock file may become undeletable because the reader was never closed.

5. **JoinNodes Corrupts HTML Content**
   `JoinNodesAction` uses regex-based `<body>` tag manipulation to merge HTML content from multiple nodes. The regex pattern doesn't account for `<body>` tags with attributes like `<body style="...">`, and the split with `-2` parameter can produce incomplete HTML fragments, resulting in malformed node content.
   _Reproduction:_ Launch FreeMind → File → New → create 3 sibling child nodes → set each to "Use Rich Formatting" (Alt+R) → add styled text to each → select all three → Tools → Join Nodes → observe the merged content may have broken HTML with missing or duplicated body tags.

6. **Encryption Uses Cryptographically Broken DES Algorithm**
   The encryption in `EncryptedMindMapNode` uses `SingleDesEncrypter` with DES (56-bit key), `Math.random()` for salt generation (predictable LCG), and MD5 for key derivation — all deprecated since the 2000s. Password comparison uses early-exit character comparison vulnerable to timing attacks.
   _Reproduction:_ Launch FreeMind → create a child node → right-click → Encrypt Node → enter password "ab" (minimum 2 characters accepted) → the content is "encrypted" with DES which is brute-forceable in hours on modern hardware.

7. **Underlined Formatting Action Exists but Is Non-Functional**
   `UnderlinedAction` is defined as a Java class and keyboard shortcuts exist, but the view layer never renders underlined text. The code comment states "Underlined is not implemented in the view yet." The action toggles a model flag that has no visual effect.
   _Reproduction:_ Launch FreeMind → File → New → add a child node with text → if the user discovers the underline action through properties or scripts, toggling it has no visual effect on the node.

8. **Find & Replace Menu Item Is a Non-Functional Placeholder**
   The Edit menu contains a "Find & Replace" entry defined in `mindmap_menus.xml` but it maps to an empty action placeholder. Clicking it does nothing.
   _Reproduction:_ Launch FreeMind → Edit menu → observe "Find & Replace" entry → click it → nothing happens.

9. **Report Bug Menu Item Is a Non-Functional Placeholder**
   Help → Report Bug exists in the menu XML configuration but maps to an empty placeholder. Users seeking to report bugs through the application cannot.
   _Reproduction:_ Launch FreeMind → Help → Report Bug → nothing happens.

10. **Export Text/Picture/PDF Placeholders in Export Submenu**
    The File → Export submenu contains entries for "Export as Text", "Export as Picture", and "Export as PDF" that are empty placeholders in the menu XML. While plugin-based equivalents exist (ExportToImage, ExportPdf), the built-in menu entries do nothing, creating confusion about which export options actually work.
    _Reproduction:_ Launch FreeMind → File → Export → observe placeholder entries mixed with functional ones — clicking a placeholder does nothing with no error message.

11. **EditServer Socket Connection Leak**
    In `EditServer.java`, the `finally` block that should close client socket connections is entirely commented out. Each incoming IPC connection leaves an unclosed socket, eventually exhausting available ports.
    _Reproduction:_ Launch FreeMind → use external tools or scripts that send commands to FreeMind's edit server port repeatedly → observe connection count growing without cleanup.

12. **Auto-Save Can Silently Fail Without User Notification**
    The `DoAutomaticSave` timer task catches exceptions during auto-save but only logs them — no user-visible notification appears. If the auto-save directory becomes full or inaccessible, users believe their work is being saved when it is not.
    _Reproduction:_ Launch FreeMind → create a map → edit it extensively → fill the temp directory (or make it read-only) → observe no warning that auto-save has failed.

13. **Undo History Unbounded Memory Consumption**
    While the undo stack has a configurable entry limit (default 100), each entry stores a full XML serialization of the action. For large maps with complex operations (paste large branches, bulk formatting), undo entries can consume significant memory. No memory-based limit exists, and no warning is shown before `OutOfMemoryError`.
    _Reproduction:_ Launch FreeMind → open a very large .mm file (1000+ nodes) → perform many paste operations of large branches → observe memory consumption growing with each undo entry → eventually OutOfMemoryError.

14. **Clipboard Owner Set to Null**
    `MindMapController.setClipboardContents()` passes `null` as the clipboard owner, meaning FreeMind never receives `lostOwnership` notifications. This violates the clipboard ownership protocol and can cause data loss if another application claims the clipboard between copy and paste.
    _Reproduction:_ Launch FreeMind → copy a node (Ctrl+C) → switch to another application that also copies content → switch back to FreeMind → Ctrl+V may paste stale or corrupted data in edge cases.

15. **Root Node Deletion Prevention Is Inconsistent with Multi-Selection**
    `DeleteChildAction` checks if a selected node is root and shows an error, but when multiple nodes are selected (including root), the error appears but the loop continues processing remaining nodes. The behavior is inconsistent — some nodes get deleted, root does not, and the user gets a confusing partial deletion result.
    _Reproduction:_ Launch FreeMind → File → New → add child nodes → Ctrl+A (Select All, including root) → press Delete → observe error message about root, but child nodes may still be deleted, producing an inconsistent result.

---

## Q3. Features/Functions Lacking Instructions or Documentation

1. **Space Bar Toggle Folding — Completely Undiscoverable**
   Pressing Space on a selected node toggles folding (expand/collapse children). This is one of the most frequently needed operations but has no tooltip, no menu-visible shortcut, and no onscreen hint. The shortcut is only documented in Help → Keyboard Shortcuts (a PDF/HTML file the user must actively seek out).
   _Reproduction:_ Launch FreeMind → File → New → add several child nodes → observe there is no visible indicator of how to fold/unfold a node — new users typically try double-click (which enters edit mode instead).

2. **E/D/S/F Navigation Keys — Hidden Vim-Style Navigation**
   The keys E (up), D (down), S (left), F (right) provide alternative keyboard navigation. This is never mentioned in menus, tooltips, or the main UI. A user would only discover this by reading the keyboard shortcuts documentation or accidentally pressing these keys.
   _Reproduction:_ Launch FreeMind → File → New → create several nodes → press E, D, S, F → observe node selection moves, but there is zero in-app indication these shortcuts exist.

3. **Pattern Application via F1–F9 — No Visual Indicator**
   Pressing F1 through F9 applies predefined formatting patterns (styles) to selected nodes. There is no indication in the UI that patterns exist, what each F-key pattern contains, or how to edit them. The Format → Physical Style submenu exists but doesn't show which key maps to which pattern.
   _Reproduction:_ Launch FreeMind → File → New → select a node → press F3 → observe the node's formatting changes, but there was no prior indication what F3 would do or what patterns are available.

4. **Ctrl+Drag Changes Parent vGap Instead of Moving Node**
   Holding Ctrl while dragging a node's motion handle changes the vertical gap between the parent's children (vGap) rather than moving the individual node. This modifier behavior is never explained in the UI. Double-clicking the handle resets positioning — also undocumented in the interface.
   _Reproduction:_ Launch FreeMind → File → New → add child nodes → Ctrl+drag the handle near a node → observe all sibling spacing changes rather than the individual node moving — confusing with no explanation.

5. **Rich Text vs Plain Text Mode — No Visual Indicator of Current Mode**
   Nodes can be in "Rich Formatting" (HTML) or "Plain Text" mode, toggled via Alt+R and Alt+P. There is no visual indicator on a node showing which mode it's in. Users may accidentally be in rich text mode and wonder why their text renders differently, or be in plain mode and wonder why formatting doesn't apply.
   _Reproduction:_ Launch FreeMind → File → New → add a child node → press Alt+R → observe no visual change — the node doesn't indicate it's now in rich text mode until the user enters the editor.

6. **Arrow Link Creation (Ctrl+L) — No Instructions on Two-Node Selection**
   Creating an arrow link requires selecting two nodes and pressing Ctrl+L, but the UI provides no guidance on how to select two nodes (Ctrl+click) or what will happen. If only one node is selected, the action either fails silently or produces an unexpected self-referencing link.
   _Reproduction:_ Launch FreeMind → File → New → add two child nodes → select one → press Ctrl+L → observe confusing behavior or error — no tooltip explains you need to select a second node first.

7. **Node Attributes (Alt+F9) — Completely Undocumented in UI**
   Nodes support key-value attributes accessible via Alt+F9, but there is no mention of attributes in the main menu, no tooltip, and the feature appears only in the right-click context menu as "Edit Attributes." What attributes are for, how they interact with filtering, and their format are unexplained.
   _Reproduction:_ Launch FreeMind → File → New → add a child node → press Alt+F9 → observe an attribute editor appears with no explanation of what attributes are, what format to use, or why you'd want them.

8. **Cloud Feature — Toggle Without Explanation**
   The Cloud toggle (Ctrl+Shift+B) wraps a node and its children in a colored cloud shape. There is no tooltip explaining what a "cloud" is in the mind mapping context, what it's useful for, or how to change its color (separate Cloud Color action, also undocumented).
   _Reproduction:_ Launch FreeMind → File → New → select root → Insert → Toggle Cloud → observe a grey cloud shape appears around the node, but no explanation of its purpose or how to customize it.

9. **File Mode (Alt+3) — Mysterious Alternate Mode**
   FreeMind supports three modes: MindMap, Browse, and File. File mode allows browsing the filesystem as a mind map. The Maps → Modes menu shows these options but provides no explanation of what each mode does, when to use them, or how they differ. File mode in particular was partially implemented (stubbed methods in FileMapModel).
   _Reproduction:_ Launch FreeMind → Maps → Modes → File → observe the interface changes to a file browser view with no explanation, no help, and partially broken functionality.

10. **Encryption — No Documentation on Algorithm or Strength**
    The node encryption feature prompts for a password (minimum 2 characters) but provides no information about the encryption algorithm used, its strength, or the risk of data loss if the password is forgotten. Users have no way to know their data is protected by deprecated DES encryption.
    _Reproduction:_ Launch FreeMind → File → New → add a child node → right-click → Encrypt → enter password → observe no indication of encryption strength, algorithm, or that the 2-character minimum is insecure.

11. **Scripting Engine — No In-App Documentation**
    The scripting system (accessible via Tools → Scripting when the plugin is loaded) allows executing Groovy/BeanShell scripts but provides no in-app documentation on available API methods, security restrictions, or example scripts. The ScriptEditorPanel is a bare text area.
    _Reproduction:_ Launch FreeMind → Tools → Scripting → Edit Script → observe a blank script editor with no syntax highlighting, no API reference, no examples, and no help button.

12. **Node Styles (Fork, Bubble, Combined, As Parent) — No Visual Preview**
    The Format → Style submenu offers Fork and Bubble styles but doesn't explain or preview what each looks like. "Combined" and "As Parent" are additional styles defined in properties but may not appear in menus. There's no tooltip or visual preview of what choosing a style will do.
    _Reproduction:_ Launch FreeMind → File → New → add a node → right-click → Format → Style → observe "Fork" and "Bubble" options with no preview, no description, and no indication of what "Combined" or "As Parent" do.

13. **Export Formats — No Documentation of What Each Produces**
    The Export submenu lists HTML, Branch to HTML, XSLT, PDF, SVG, PNG, JPEG, OpenOffice Writer, and others, but none have descriptions explaining the output format, use cases, or limitations. Users must try each to learn what they produce.
    _Reproduction:_ Launch FreeMind → File → Export → observe a list of export formats with no descriptions, no previews, and no "Learn more" links.

14. **Filter Toolbar — No Explanation of Filter Logic**
    The filter toolbar can be shown via View → Show Filter Toolbar, but the filter composer's AND/OR/NOT condition builder has no built-in help explaining the available condition types (text match, icon match, attribute match) or how Boolean logic combines them.
    _Reproduction:_ Launch FreeMind → View → Show Filter Toolbar → click the filter compose icon → observe a complex condition builder with dropdown menus of condition types and Boolean operators but no explanation of what each does.

15. **Time Management Plugin — Calendar Marking UI Is Unexplained**
    The Time Management plugin (if loaded) offers a CalendarMarkingDialog with date pickers, recurrence settings, and color coding. There is no documentation within the dialog explaining how calendar markers relate to nodes, where they are stored, or how to view/remove them later.
    _Reproduction:_ Launch FreeMind → Tools → Time Management → Calendar Markers → observe date pickers and recurrence options with no explanation of their purpose or how they interact with the mind map.

---

## Q4. New Features/Functions That Could Improve the User Experience

1. **Interactive First-Run Tutorial / Onboarding Wizard**
   Currently FreeMind opens to a blank map with no guidance. A step-by-step overlay tutorial showing how to add nodes (Insert key), edit text (F2), fold branches (Space), and navigate (arrow keys) would dramatically reduce the learning curve. Observation: the hidden keyboard shortcuts (Space for fold, E/D/S/F for navigation, F1-F9 for patterns) are powerful but completely undiscoverable without reading external documentation.
   _Reason:_ The audit revealed 15+ hidden keyboard shortcuts that first-time users would never find, and zero onboarding exists — not even a "Did you know?" tooltip.

2. **Real-Time Collaboration with Conflict Resolution**
   The existing collaboration features (socket-based and Jabber) are partially implemented, require manual server configuration, and lack visual status indicators. A modern real-time collaboration feature with presence indicators (showing who is editing which node), automatic conflict resolution, and a simple "Share" button would transform FreeMind from a single-user tool to a team tool.
   _Reason:_ The codebase contains three separate collaboration systems (database, Jabber/XMPP, socket) — all incomplete. Users clearly wanted this feature but the implementations never matured.

3. **Dark Mode / Theme System**
   FreeMind has hardcoded colors throughout (white background `#ffffff`, black text `#000000`, grey edges `#808080`). A theme system supporting at least light/dark modes would modernize the application and reduce eye strain for extended use. The existing `freemind.properties` system could be extended with theme profiles.
   _Reason:_ All colors are defined in `freemind.properties` as hardcoded hex values with no grouping or theme abstraction. Every modern application supports dark mode.

4. **Searchable Command Palette (Ctrl+Shift+P)**
   With 74+ action classes, 100+ keyboard shortcuts, and 150+ menu items, feature discovery is a major problem. A searchable command palette (like VS Code's Ctrl+Shift+P) would let users type action names to find and execute any command, discover shortcuts, and access features buried in submenus.
   _Reason:_ The audit found 9 empty placeholder menu categories, features only accessible via undocumented shortcuts, and a preference panel with cryptic labels — a command palette would solve all discoverability issues.

5. **Markdown Import/Export**
   FreeMind supports HTML, XML, PDF, SVG, and image exports but has no Markdown support. Given that Markdown is now the dominant format for notes, documentation, and knowledge bases, import from and export to Markdown (with indentation representing hierarchy) would greatly expand FreeMind's utility in modern workflows.
   _Reason:_ The existing XSLT-based export system is architecturally ready for a Markdown template, and the codebase already handles text export (albeit as a placeholder).

6. **Node Templates / Quick-Insert Library**
   The pattern system allows saving formatting styles but not node content templates. A template library where users can save and insert pre-structured subtrees (e.g., "Meeting Notes" with Date, Attendees, Action Items children; or "SWOT Analysis" with four quadrant branches) would save time for repeated use cases.
   _Reason:_ The ManagePatternsPopupDialog handles formatting patterns only — no content templates exist despite mind mapping being heavily template-driven in practice.

7. **Modern Encryption (AES-256 with Key Derivation)**
   The current DES encryption is broken. Replacing it with AES-256-GCM, using `SecureRandom` for IV/salt generation, PBKDF2/Argon2 for key derivation, and enforcing a minimum 8-character password would bring security to modern standards. A migration path for existing DES-encrypted nodes should be provided.
   _Reason:_ The audit confirmed DES with `Math.random()` salt and MD5 key derivation — all deprecated. Password minimum is 2 characters. This is a serious security gap for any user storing sensitive data.

8. **Visual Tab Bar for Open Maps**
   Replace the Maps menu radio-button navigation with a persistent, draggable tab bar showing all open maps. Each tab should show the map name, a modified indicator (dot or asterisk), and a close button. Ctrl+Tab for quick switching. This mirrors every modern multi-document application.
   _Reason:_ The current Maps menu requires two clicks to switch between maps, offers no visual indicator of which maps are open or modified, and cannot be reordered.

9. **Node Linking Visualization Panel**
   Arrow links between nodes are created with Ctrl+L but there is no overview panel showing all links in a map. A side panel listing all links with source/target, link type, and click-to-navigate would help users manage cross-references in complex maps.
   _Reason:_ The audit found AddArrowLinkAction, AddLocalLinkAction, and GotoLinkNodeAction but no way to see an overview of all links — users must visually scan the entire map to find link arrows.

10. **Auto-Save Status Indicator**
    Currently, auto-save runs silently on a timer with no visual feedback. Adding a small status icon showing last auto-save time, next scheduled save, and whether auto-save succeeded or failed would give users confidence their work is protected — especially since the audit found that auto-save failures are silently logged without user notification.
    _Reason:_ `DoAutomaticSave` logs errors but never shows them to the user. A status indicator would surface these silent failures.

11. **Keyboard Shortcut Cheat Sheet Overlay (Shift+?)**
    An in-app overlay showing all keyboard shortcuts grouped by category (navigation, editing, formatting, view) that appears when pressing Shift+? would make the extensive shortcut system discoverable without leaving the application. Currently, the only shortcut reference is an external PDF/HTML file.
    _Reason:_ The audit found 100+ keyboard shortcuts of which fewer than 30 are visible in menus. A cheat sheet overlay would solve the discoverability gap identified in Q3.

12. **Presentation / Slideshow Mode**
    A mode that steps through branches sequentially (depth-first or breadth-first), zooming into each sub-tree as a "slide," would enable using mind maps directly for presentations. This would combine FreeMind's planning strength with delivery capability.
    _Reason:_ The existing fold/unfold mechanism (Space key) and zoom system already provide the building blocks — they just need orchestration into a linear presentation flow.

13. **Accessibility Support (Screen Reader, High Contrast)**
    The audit found zero accessibility features: no `AccessibleContext` implementations, no screen reader support, no high-contrast mode, no focus indicators beyond the default selection highlight. Adding ARIA-like accessible names to nodes, keyboard focus indicators, and a high-contrast theme would make FreeMind usable for users with visual impairments.
    _Reason:_ Grep for "accessibility", "a11y", or "screenreader" returned 0 results across the entire codebase.

14. **Smart Node Suggestions / Auto-Complete**
    When creating or editing a node, suggesting completions based on existing node text in the map (or from a user dictionary) would speed up entry and maintain consistency. For example, if "Project Alpha" exists as a node, typing "Proj" in a new node could offer it as a suggestion.
    _Reason:_ The spell-check integration (JOrtho) shows the infrastructure for text analysis during editing already exists — extending it with map-aware suggestions would be a natural evolution.

15. **Version History / Map Diffing**
    The auto-save mechanism creates timestamped backup files but provides no way to compare versions or revert to a specific point. A version history panel showing past auto-saves with visual diffs (highlighting added/removed/modified nodes) would provide safety and auditability.
    _Reason:_ `DoAutomaticSave` already creates multiple timestamped copies (`FM_<mapname>_<timestamp>.mm`) — the backup infrastructure exists but there is no UI to browse or compare them.
