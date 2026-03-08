Ran on Claude Opus 4.6

```
You are a senior business analyst and software architect with deep expertise in requirements engineering, UX design patterns, and object-oriented systems. You have extensive experience writing Use Case Requirements documents for desktop applications and enterprise software.

Your task is to read the UX audit report at claude-prompts/ux-audit-report.md and generate a comprehensive Use Case Requirements document based on the improvements, defects, missing documentation, and new feature opportunities identified in that report.

Every use case must represent an improvement, fix, or new capability beyond what the current application already does. Do not write use cases for existing functionality that works as intended. Ground each use case in specific findings from the audit report.

Identify as many meaningful use cases as possible — focus on use cases that represent real, significant interactions or goals a user or system actor would care about. Include both use cases for fixing defects and use cases for new/improved features. Avoid trivial or redundant nitpicks, but do include genuinely small-but-meaningful use cases if they represent a distinct user goal.

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

Group related use cases under clear section headings that map back to the audit report's categories (e.g. UX Improvements, Defect Fixes, Documentation Gaps, New Features). Aim for completeness and depth — 30+ well-reasoned use cases is the target. Each field should be concrete and specific, not vague generalisations.

Once complete, save the full document as a Markdown file at claude-prompts/use-case-requirements.md, creating the directory if it doesn't exist.
```

# FreeMind — Use Case Requirements (Proposed Improvements)

**Project:** FreeMind Mind Mapping Application (v1.1.0)
**Date:** 2026-03-08
**Purpose:** Use cases for improvements, new features, and defect resolutions beyond the current application. Informed by a prior UX audit and independent codebase analysis.

---

## Table of Contents

1. [Onboarding & Discoverability](#1-onboarding--discoverability)
2. [Node Editing Improvements](#2-node-editing-improvements)
3. [Search & Replace](#3-search--replace)
4. [Visual Feedback & Status](#4-visual-feedback--status)
5. [Export & Import Modernisation](#5-export--import-modernisation)
6. [Navigation & Map Management](#6-navigation--map-management)
7. [Formatting & Styling Workflow](#7-formatting--styling-workflow)
8. [Encryption & Security](#8-encryption--security)
9. [Collaboration](#9-collaboration)
10. [Accessibility](#10-accessibility)
11. [Preferences & Configuration](#11-preferences--configuration)
12. [Printing & Preview](#12-printing--preview)
13. [Reliability & Error Handling](#13-reliability--error-handling)
14. [Advanced Features](#14-advanced-features)

---

## 1. Onboarding & Discoverability

### Use Case 1: Complete a First-Run Interactive Tutorial

| Field                 | Description                                                                                                                                                                                                                                 |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Learn FreeMind's core operations (add node, edit text, fold/unfold, navigate) through a guided, in-app walkthrough on first launch                                                                                                          |
| Scope & Level         | System / User-goal                                                                                                                                                                                                                          |
| Preconditions         | The user has launched FreeMind for the first time (or has reset the "show tutorial" flag in preferences)                                                                                                                                    |
| Success End Condition | The user has completed a step-by-step overlay tutorial covering: creating child nodes (Insert), editing text (F2), folding (Space), keyboard navigation (arrows and E/D/S/F), and applying icons — and can dismiss the tutorial permanently |
| Failed End Condition  | The user skips the tutorial; a "Show Tutorial Again" option remains accessible in the Help menu                                                                                                                                             |
| Actors                | Primary: First-Time User                                                                                                                                                                                                                    |
| Trigger               | First launch of FreeMind after installation (no user properties file exists)                                                                                                                                                                |

### Use Case 2: Open a Searchable Command Palette

| Field                 | Description                                                                                                                                                                                  |
| --------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Find and execute any FreeMind action by typing its name, without needing to remember its menu location or keyboard shortcut                                                                  |
| Scope & Level         | System / User-goal                                                                                                                                                                           |
| Preconditions         | FreeMind is running with a mind map open                                                                                                                                                     |
| Success End Condition | A floating search dialog shows all available actions filtered by the user's typed query; selecting an action executes it immediately and displays its keyboard shortcut for future reference |
| Failed End Condition  | No matching action is found; the palette shows "No results"                                                                                                                                  |
| Actors                | Primary: Mind Map Author                                                                                                                                                                     |
| Trigger               | User presses Ctrl+Shift+P (or a configurable shortcut)                                                                                                                                       |

### Use Case 3: View a Keyboard Shortcut Cheat Sheet Overlay

| Field                 | Description                                                                                                                           |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | See all available keyboard shortcuts grouped by category (navigation, editing, formatting, view) without leaving the application      |
| Scope & Level         | System / User-goal                                                                                                                    |
| Preconditions         | FreeMind is running                                                                                                                   |
| Success End Condition | A non-modal overlay displays all shortcuts organised by function; the overlay can be dismissed by pressing Escape or clicking outside |
| Failed End Condition  | N/A (the overlay is a static display)                                                                                                 |
| Actors                | Primary: Mind Map Author                                                                                                              |
| Trigger               | User presses Shift+? or selects Help → Keyboard Shortcuts Overlay                                                                     |

---

## 2. Node Editing Improvements

### Use Case 4: Edit a Node with Auto-Wrapping Inline Text Field

| Field                 | Description                                                                                                                                                                             |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Type or edit node text in-place with the text field dynamically growing in width and height to accommodate content, including word wrapping for long text                               |
| Scope & Level         | System / User-goal                                                                                                                                                                      |
| Preconditions         | A node is selected in MindMap mode                                                                                                                                                      |
| Success End Condition | The inline editor wraps text at a configurable maximum width, grows vertically as needed, and shows a subtle visual hint ("Alt+Enter for full editor") when content exceeds a threshold |
| Failed End Condition  | The user's text is accepted but the field reverts to old fixed-width behaviour as a fallback                                                                                            |
| Actors                | Primary: Mind Map Author                                                                                                                                                                |
| Trigger               | User presses F2 or begins typing on a selected node                                                                                                                                     |

### Use Case 5: Use Find and Replace Across Nodes

| Field                 | Description                                                                                                                                                                        |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Find all occurrences of a text string across node labels and notes, and replace them individually or in bulk                                                                       |
| Scope & Level         | System / User-goal                                                                                                                                                                 |
| Preconditions         | A mind map is open with at least one node containing text                                                                                                                          |
| Success End Condition | All matching occurrences are highlighted; the user can step through matches one-by-one (Replace / Skip) or replace all at once; changes are registered as a single undoable action |
| Failed End Condition  | No matches found; a "not found" message is displayed                                                                                                                               |
| Actors                | Primary: Mind Map Author                                                                                                                                                           |
| Trigger               | User selects Edit → Find & Replace (Ctrl+H) — replacing the current non-functional placeholder                                                                                     |

### Use Case 6: Receive Smart Node Text Suggestions

| Field                 | Description                                                                                                                                        |
| --------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | While typing a new or edited node's text, see auto-complete suggestions drawn from existing node labels in the current map to maintain consistency |
| Scope & Level         | System / Subfunction                                                                                                                               |
| Preconditions         | The user is in inline edit mode; the map contains existing nodes with text                                                                         |
| Success End Condition | A dropdown of matching existing node texts appears below the cursor; selecting a suggestion fills in the text                                      |
| Failed End Condition  | No matching text exists; no suggestions are shown (unobtrusive)                                                                                    |
| Actors                | Primary: Mind Map Author                                                                                                                           |
| Trigger               | User types 3+ characters in the inline node editor                                                                                                 |

### Use Case 7: Split a Node Interactively

| Field                 | Description                                                                                                                       |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Split a node's text at user-chosen split points (not just line breaks) into multiple sibling or child nodes via a preview dialog  |
| Scope & Level         | System / User-goal                                                                                                                |
| Preconditions         | A node with multi-segment text is selected                                                                                        |
| Success End Condition | A dialog shows the text with proposed split points that the user can adjust; upon confirmation, new nodes are created accordingly |
| Failed End Condition  | User cancels the dialog; the node is unchanged                                                                                    |
| Actors                | Primary: Mind Map Author                                                                                                          |
| Trigger               | User selects Edit → Split Node (improved version of current action)                                                               |

---

## 3. Search & Replace

### Use Case 8: Filter the Map with Live Preview

| Field                 | Description                                                                                                                                                                       |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Build a filter condition (by icon, text content, or attribute) and immediately see which nodes match before committing the filter                                                 |
| Scope & Level         | System / User-goal                                                                                                                                                                |
| Preconditions         | A mind map is open; the filter toolbar is visible                                                                                                                                 |
| Success End Condition | As the user builds conditions in the FilterComposerDialog, matching nodes are highlighted in real time on the map behind the dialog; applying the filter hides non-matching nodes |
| Failed End Condition  | User cancels the dialog; no filter is applied, and the preview highlighting is removed                                                                                            |
| Actors                | Primary: Mind Map Author                                                                                                                                                          |
| Trigger               | User opens the Filter Composer from the filter toolbar                                                                                                                            |

---

## 4. Visual Feedback & Status

### Use Case 9: View Auto-Save Status in the Status Bar

| Field                 | Description                                                                                                                                              |
| --------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | See at a glance whether auto-save is active, when the last save occurred, and whether the most recent auto-save succeeded or failed                      |
| Scope & Level         | System / Subfunction                                                                                                                                     |
| Preconditions         | Auto-save is enabled in preferences                                                                                                                      |
| Success End Condition | A persistent status bar icon shows: a green tick with "Saved 2 min ago" on success, or a red warning icon with a tooltip explaining the failure on error |
| Failed End Condition  | Auto-save is disabled; the indicator shows "Auto-save off"                                                                                               |
| Actors                | Primary: Mind Map Author                                                                                                                                 |
| Trigger               | Automatic — updates after each auto-save cycle completes (or fails)                                                                                      |

### Use Case 10: See a Visual Indicator for Rich Text vs Plain Text Mode

| Field                 | Description                                                                                                                                                |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Know at a glance whether the currently selected node is in Rich Text (HTML) or Plain Text mode, without entering the editor                                |
| Scope & Level         | System / Subfunction                                                                                                                                       |
| Preconditions         | A node is selected                                                                                                                                         |
| Success End Condition | A small badge or status bar label shows "Rich Text" or "Plain Text" for the selected node; toggling mode (Alt+R / Alt+P) updates the indicator immediately |
| Failed End Condition  | N/A (always displays current state)                                                                                                                        |
| Actors                | Primary: Mind Map Author                                                                                                                                   |
| Trigger               | User selects a node or toggles the formatting mode                                                                                                         |

### Use Case 11: View an Undo/Redo History Panel

| Field                 | Description                                                                                                                                                                                                |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | See a list of all undoable actions with descriptions, and jump to any specific past state rather than stepping through one action at a time                                                                |
| Scope & Level         | System / User-goal                                                                                                                                                                                         |
| Preconditions         | At least one undoable action has been performed                                                                                                                                                            |
| Success End Condition | A side panel or dropdown lists recent actions (e.g., "Delete node 'Budget'", "Change font to Arial", "Add child node"); clicking an entry undoes/redoes all actions up to that point in a single operation |
| Failed End Condition  | The undo stack is empty; the panel shows "No actions to undo"                                                                                                                                              |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                   |
| Trigger               | User clicks the dropdown arrow next to the Undo/Redo toolbar buttons, or selects Edit → Undo History                                                                                                       |

### Use Case 12: See Node Link Overview Panel

| Field                 | Description                                                                                                                               |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | View all arrow links and internal links in the map in a summary panel, with source/target node names and click-to-navigate                |
| Scope & Level         | System / User-goal                                                                                                                        |
| Preconditions         | The map contains at least one arrow link or local link                                                                                    |
| Success End Condition | A side panel lists all links with columns: Source Node, Target Node, Link Type; clicking a row navigates to and selects both linked nodes |
| Failed End Condition  | No links exist; the panel shows "No links in this map"                                                                                    |
| Actors                | Primary: Mind Map Author                                                                                                                  |
| Trigger               | User selects View → Link Overview Panel                                                                                                   |

---

## 5. Export & Import Modernisation

### Use Case 13: Export a Mind Map as Markdown

| Field                 | Description                                                                                                                                                                                                         |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Export the mind map as a Markdown document where hierarchy is represented by heading levels and/or indented lists                                                                                                   |
| Scope & Level         | System / User-goal                                                                                                                                                                                                  |
| Preconditions         | A mind map is open                                                                                                                                                                                                  |
| Success End Condition | A `.md` file is created where root = H1, level-1 children = H2 (or list items), and so on; node notes are included as body text under each heading; links and icons are preserved as Markdown syntax where possible |
| Failed End Condition  | Export fails due to I/O error                                                                                                                                                                                       |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                            |
| Trigger               | User selects File → Export → Markdown                                                                                                                                                                               |

### Use Case 14: Import a Markdown File as a Mind Map

| Field                 | Description                                                                                                                    |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Convert a Markdown document into a mind map where headings become parent nodes and list items or paragraphs become child nodes |
| Scope & Level         | System / User-goal                                                                                                             |
| Preconditions         | A valid `.md` file exists on the filesystem                                                                                    |
| Success End Condition | A new mind map is created with heading hierarchy mapped to node depth; body text is stored as node notes                       |
| Failed End Condition  | The file is not valid Markdown or is empty; an error dialog is shown                                                           |
| Actors                | Primary: Mind Map Author                                                                                                       |
| Trigger               | User selects File → Import → Markdown                                                                                          |

### Use Case 15: Preview an Export Before Saving

| Field                 | Description                                                                                                                                                           |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | See a WYSIWYG preview of the export output (HTML, PDF, image) before committing to file, allowing adjustments to settings without trial-and-error iteration           |
| Scope & Level         | System / User-goal                                                                                                                                                    |
| Preconditions         | A mind map is open; the user has selected an export format                                                                                                            |
| Success End Condition | A preview dialog renders the export output; the user can adjust settings (margins, scaling, orientation) and see the preview update; clicking "Export" saves the file |
| Failed End Condition  | User cancels the preview dialog; no file is created                                                                                                                   |
| Actors                | Primary: Mind Map Author                                                                                                                                              |
| Trigger               | User selects any export action (PDF, PNG, JPEG, HTML, SVG)                                                                                                            |

### Use Case 16: Remove Non-Functional Export/Import Placeholders

| Field                 | Description                                                                                                                                                                                                                                                |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Eliminate empty menu entries ("Export as Text", "Export as Picture", "Export as PDF" placeholders, "Find & Replace" stub, "Report Bug" stub) that do nothing when clicked, replacing them with either functional implementations or removing them entirely |
| Scope & Level         | System / User-goal                                                                                                                                                                                                                                         |
| Preconditions         | FreeMind is running                                                                                                                                                                                                                                        |
| Success End Condition | Every menu item either performs a real action or is removed; no "dead" menu entries exist                                                                                                                                                                  |
| Failed End Condition  | N/A (code change only)                                                                                                                                                                                                                                     |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                                                   |
| Trigger               | User opens any menu — all entries are functional                                                                                                                                                                                                           |

---

## 6. Navigation & Map Management

### Use Case 17: Switch Maps via a Visual Tab Bar

| Field                 | Description                                                                                                                                                                                                                           |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | See all open maps as draggable tabs in a persistent tab bar, each showing the map name, a modified indicator, and a close button                                                                                                      |
| Scope & Level         | System / User-goal                                                                                                                                                                                                                    |
| Preconditions         | Two or more maps are open                                                                                                                                                                                                             |
| Success End Condition | A tab bar is visible below the toolbar; clicking a tab switches to that map; a dot/asterisk indicates unsaved changes; the close button on each tab closes that map (with save prompt if modified); tabs can be reordered by dragging |
| Failed End Condition  | Only one map is open; the tab bar shows a single tab (still functional)                                                                                                                                                               |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                              |
| Trigger               | User opens a second map, making the tab bar appear; Ctrl+Tab cycles through tabs                                                                                                                                                      |

### Use Case 18: Use Snap-to-Grid and Alignment Guides When Repositioning Nodes

| Field                 | Description                                                                                                                                                                                                                    |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Align nodes precisely during drag-and-drop repositioning using visual alignment guides and optional snap-to-grid                                                                                                               |
| Scope & Level         | System / Subfunction                                                                                                                                                                                                           |
| Preconditions         | A node is being dragged in MindMap mode                                                                                                                                                                                        |
| Success End Condition | Blue guide lines appear when the dragged node aligns horizontally or vertically with sibling or cousin nodes; the node snaps to the guide when released within a threshold distance; snap-to-grid can be toggled via View menu |
| Failed End Condition  | User drags outside any alignment zone; the node is placed freely (current behaviour)                                                                                                                                           |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                       |
| Trigger               | User begins dragging a node                                                                                                                                                                                                    |

### Use Case 19: Enter Zoom Level Directly

| Field                 | Description                                                                                                        |
| --------------------- | ------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Type an arbitrary zoom percentage (e.g., 110%, 175%) rather than being limited to the fixed zoom level presets     |
| Scope & Level         | System / Subfunction                                                                                               |
| Preconditions         | A mind map is displayed                                                                                            |
| Success End Condition | The user can click the zoom dropdown, type a percentage, press Enter, and the map renders at that exact zoom level |
| Failed End Condition  | The user types a value outside the valid range (10–500%); the input is clamped and a tooltip shows the valid range |
| Actors                | Primary: Mind Map Author                                                                                           |
| Trigger               | User clicks the zoom combo box in the toolbar and types a number                                                   |

### Use Case 20: Browse and Restore Map Version History

| Field                 | Description                                                                                                                                                                                                                        |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | View a list of auto-saved backup versions of the current map, compare them visually (showing added/removed/modified nodes), and restore a past version                                                                             |
| Scope & Level         | System / User-goal                                                                                                                                                                                                                 |
| Preconditions         | Auto-save is enabled; at least one backup file exists for the current map                                                                                                                                                          |
| Success End Condition | A dialog lists all timestamped backups; selecting one shows a visual diff against the current version; the user can restore the selected version (which becomes the current map, with the pre-restore state saved as a new backup) |
| Failed End Condition  | No backups exist; the dialog shows "No backup versions found"                                                                                                                                                                      |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                           |
| Trigger               | User selects File → Version History                                                                                                                                                                                                |

### Use Case 21: Enter Presentation / Slideshow Mode

| Field                 | Description                                                                                                                                                                                                                                    |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Step through branches of the mind map sequentially in full screen, with each branch zoomed and centred as a "slide", for use in meetings or talks                                                                                              |
| Scope & Level         | System / User-goal                                                                                                                                                                                                                             |
| Preconditions         | A mind map is open with at least one level of children                                                                                                                                                                                         |
| Success End Condition | FreeMind enters full-screen mode; the root node is shown first; pressing Right Arrow or Space advances to the next branch (depth-first or breadth-first, configurable); pressing Left Arrow goes back; pressing Escape exits presentation mode |
| Failed End Condition  | The map has no children; presentation mode shows only the root and exits                                                                                                                                                                       |
| Actors                | Primary: Presenter / Mind Map Author                                                                                                                                                                                                           |
| Trigger               | User selects View → Presentation Mode or presses F5                                                                                                                                                                                            |

---

## 7. Formatting & Styling Workflow

### Use Case 22: Apply Edge Formatting to an Entire Branch

| Field                 | Description                                                                                                                            |
| --------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Set edge style, width, and colour for a node and all its descendants in a single action, rather than formatting each node individually |
| Scope & Level         | System / User-goal                                                                                                                     |
| Preconditions         | A node with children is selected                                                                                                       |
| Success End Condition | All edges in the selected subtree adopt the chosen style, width, and colour; the action is undoable as a single step                   |
| Failed End Condition  | The node is a leaf (only its own edge is changed, same as current behaviour)                                                           |
| Actors                | Primary: Mind Map Author                                                                                                               |
| Trigger               | User right-clicks → Format → Apply to Branch, or holds Shift while selecting an edge format option                                     |

### Use Case 23: Use a Colour Palette with Recent and Saved Colours

| Field                 | Description                                                                                                                                                               |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Quickly reuse recently picked colours and maintain a saved project palette, rather than re-picking colours from the full colour chooser each time                         |
| Scope & Level         | System / Subfunction                                                                                                                                                      |
| Preconditions         | The user has previously picked at least one colour                                                                                                                        |
| Success End Condition | The colour chooser dialog includes a "Recent Colours" row (last 10 picks) and a "Saved Palette" section where users can pin colours; the palette persists across sessions |
| Failed End Condition  | No prior colours exist; the recent row is empty (the full chooser is still available)                                                                                     |
| Actors                | Primary: Mind Map Author                                                                                                                                                  |
| Trigger               | User opens any colour chooser (node colour, edge colour, cloud colour, background colour)                                                                                 |

### Use Case 24: Preview Node Styles Before Applying

| Field                 | Description                                                                                                       |
| --------------------- | ----------------------------------------------------------------------------------------------------------------- |
| Goal                  | See a visual preview of what Fork, Bubble, Combined, and As Parent styles look like before applying one to a node |
| Scope & Level         | System / Subfunction                                                                                              |
| Preconditions         | A node is selected                                                                                                |
| Success End Condition | The style selection menu or dialog shows a small preview thumbnail of each style option next to its name          |
| Failed End Condition  | Preview rendering fails; the menu falls back to text-only labels (current behaviour)                              |
| Actors                | Primary: Mind Map Author                                                                                          |
| Trigger               | User opens Format → Style or the style section of the context menu                                                |

### Use Case 25: Use a Node Content Template Library

| Field                 | Description                                                                                                                                                                                                                 |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Insert a pre-structured subtree from a template library (e.g., "Meeting Notes" with Date/Attendees/Action Items children, "SWOT Analysis" with four branches) rather than building common structures from scratch each time |
| Scope & Level         | System / User-goal                                                                                                                                                                                                          |
| Preconditions         | At least one template is available (built-in defaults or user-created)                                                                                                                                                      |
| Success End Condition | The selected template subtree is inserted as children of the selected node; the user can edit the template content                                                                                                          |
| Failed End Condition  | No templates exist; the dialog offers to create one from the current selection                                                                                                                                              |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                    |
| Trigger               | User selects Insert → From Template or presses a configurable shortcut                                                                                                                                                      |

### Use Case 26: Search and Filter the Icon Selection Grid

| Field                 | Description                                                                                                                                                                       |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Find a specific icon by typing a keyword (e.g., "priority", "check", "star") rather than visually scanning 100+ icons in a flat grid                                              |
| Scope & Level         | System / Subfunction                                                                                                                                                              |
| Preconditions         | The icon selection dialog is open                                                                                                                                                 |
| Success End Condition | A search field at the top of the dialog filters the icon grid to show only icons whose names contain the query; category tabs group icons by type (status, arrows, symbols, etc.) |
| Failed End Condition  | No icons match the query; the grid shows "No matching icons"                                                                                                                      |
| Actors                | Primary: Mind Map Author                                                                                                                                                          |
| Trigger               | User opens the icon selection dialog and begins typing in the search field                                                                                                        |

---

## 8. Encryption & Security

### Use Case 27: Encrypt a Node with AES-256

| Field                 | Description                                                                                                                                                                                                                                                      |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Protect sensitive node content with modern AES-256-GCM encryption, PBKDF2 key derivation, and cryptographically secure random salt/IV generation — replacing the current broken DES implementation                                                               |
| Scope & Level         | System / User-goal                                                                                                                                                                                                                                               |
| Preconditions         | A node is selected; the Java runtime supports AES-256                                                                                                                                                                                                            |
| Success End Condition | The node and its children are encrypted with AES-256-GCM; the password must be at least 8 characters; a strength meter is shown during password entry; the encryption metadata (algorithm, iteration count) is stored so future versions can identify the scheme |
| Failed End Condition  | The user cancels the password dialog or enters a password shorter than 8 characters (rejected with explanation)                                                                                                                                                  |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                                                         |
| Trigger               | User selects Insert → Encrypt Node                                                                                                                                                                                                                               |

### Use Case 28: Migrate Legacy DES-Encrypted Nodes to AES-256

| Field                 | Description                                                                                                                                                                                  |
| --------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Automatically detect and offer to upgrade nodes encrypted with the deprecated DES algorithm to AES-256 upon unlock                                                                           |
| Scope & Level         | System / Subfunction                                                                                                                                                                         |
| Preconditions         | The map contains a node encrypted with the legacy DES algorithm; the user enters the correct password                                                                                        |
| Success End Condition | After unlocking, a dialog informs the user that the encryption is outdated and offers to re-encrypt with AES-256; upon acceptance, the node is re-encrypted with the new algorithm and saved |
| Failed End Condition  | The user declines the upgrade; the node remains in DES encryption (with a non-dismissible warning in the status bar)                                                                         |
| Actors                | Primary: Mind Map Author                                                                                                                                                                     |
| Trigger               | User unlocks a DES-encrypted node                                                                                                                                                            |

---

## 9. Collaboration

### Use Case 29: Share a Map for Real-Time Collaborative Editing

| Field                 | Description                                                                                                                                                                                                                                                                          |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Share the current mind map with remote users via a simple "Share" button, with presence indicators showing who is editing which node and automatic conflict resolution                                                                                                               |
| Scope & Level         | Organization / Summary                                                                                                                                                                                                                                                               |
| Preconditions         | FreeMind is running with a map open; an internet or LAN connection is available                                                                                                                                                                                                      |
| Success End Condition | A share link/code is generated; remote users connect and see the map in real-time; each user's cursor/selection is shown in a distinct colour with their name; concurrent edits to different nodes merge automatically; concurrent edits to the same node prompt conflict resolution |
| Failed End Condition  | Network is unavailable; the share button shows an error with instructions to check connectivity                                                                                                                                                                                      |
| Actors                | Primary: Session Host. Supporting: Remote Collaborators                                                                                                                                                                                                                              |
| Trigger               | User clicks the "Share" toolbar button or selects Extras → Collaboration → Share                                                                                                                                                                                                     |

### Use Case 30: See Collaborator Presence and Activity

| Field                 | Description                                                                                                                                                                                                         |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | See which collaborators are connected, which nodes they are currently viewing or editing, and a visual indicator of their cursor position                                                                           |
| Scope & Level         | System / Subfunction                                                                                                                                                                                                |
| Preconditions         | A collaborative session is active with two or more participants                                                                                                                                                     |
| Success End Condition | Each collaborator's selected node is outlined in their assigned colour; a participant panel lists connected users with status (editing, idle); a small avatar/initial badge appears on nodes being edited by others |
| Failed End Condition  | Solo editing; no presence UI is shown                                                                                                                                                                               |
| Actors                | Primary: Collaborator                                                                                                                                                                                               |
| Trigger               | Automatic — updates in real-time as collaborators interact with the map                                                                                                                                             |

---

## 10. Accessibility

### Use Case 31: Navigate and Edit a Mind Map with a Screen Reader

| Field                 | Description                                                                                                                                                                                                                   |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Use FreeMind with a screen reader (JAWS, NVDA, VoiceOver) by ensuring all nodes, menus, toolbars, and dialogs expose accessible names and roles via Java Accessibility API                                                    |
| Scope & Level         | System / User-goal                                                                                                                                                                                                            |
| Preconditions         | A screen reader is active on the system; FreeMind is running                                                                                                                                                                  |
| Success End Condition | The screen reader announces node text, depth level, number of children, fold state, and attached icons when a node is focused; all dialogs and menus are navigable via keyboard with announced labels; focus order is logical |
| Failed End Condition  | The screen reader cannot interpret the custom-rendered mind map view (graceful fallback: a text-list view of the tree is available as an alternative)                                                                         |
| Actors                | Primary: Visually Impaired User. Supporting: Screen Reader Software                                                                                                                                                           |
| Trigger               | User activates a screen reader and launches FreeMind                                                                                                                                                                          |

### Use Case 32: Switch to a High-Contrast Theme

| Field                 | Description                                                                                                                               |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Activate a high-contrast colour scheme (e.g., white text on dark background, bold outlines) for users with low vision or photosensitivity |
| Scope & Level         | System / User-goal                                                                                                                        |
| Preconditions         | FreeMind is running                                                                                                                       |
| Success End Condition | All UI elements (canvas, nodes, edges, toolbars, menus, dialogs) render in a high-contrast palette; the setting persists across sessions  |
| Failed End Condition  | The theme fails to load; FreeMind falls back to the default theme with a warning                                                          |
| Actors                | Primary: User with Visual Impairment                                                                                                      |
| Trigger               | User selects View → Theme → High Contrast, or FreeMind detects the OS high-contrast accessibility setting                                 |

---

## 11. Preferences & Configuration

### Use Case 33: Search Application Preferences by Keyword

| Field                 | Description                                                                                                                                                                   |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Find a specific setting in the preferences dialog by typing a keyword, rather than scanning through dozens of options across categories                                       |
| Scope & Level         | System / Subfunction                                                                                                                                                          |
| Preconditions         | The Preferences dialog is open                                                                                                                                                |
| Success End Condition | A search field at the top of the dialog filters the visible options to those matching the query; the matching category auto-expands; the matching option label is highlighted |
| Failed End Condition  | No matching option found; the search field shows "No results"                                                                                                                 |
| Actors                | Primary: Mind Map Author                                                                                                                                                      |
| Trigger               | User opens Preferences (Ctrl+,) and types in the search field                                                                                                                 |

### Use Case 34: Switch to Dark Mode

| Field                 | Description                                                                                                                                   |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Apply a dark colour theme to the entire application (canvas, toolbars, menus, dialogs) to reduce eye strain during extended use               |
| Scope & Level         | System / User-goal                                                                                                                            |
| Preconditions         | FreeMind is running                                                                                                                           |
| Success End Condition | The UI renders with dark backgrounds and light text throughout; node default colours adapt to the theme; the setting persists across sessions |
| Failed End Condition  | The user reverts to the default (light) theme                                                                                                 |
| Actors                | Primary: Mind Map Author                                                                                                                      |
| Trigger               | User selects View → Theme → Dark, or toggles dark mode in Preferences                                                                         |

---

## 12. Printing & Preview

### Use Case 35: Control Print Layout (Margins, Scaling, Page Breaks)

| Field                 | Description                                                                                                                                                                                   |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Configure margins, scaling factor, orientation, and page break placement before printing a large mind map                                                                                     |
| Scope & Level         | System / User-goal                                                                                                                                                                            |
| Preconditions         | A mind map is open; a printer is configured                                                                                                                                                   |
| Success End Condition | The print preview dialog shows adjustable margin handles, a scaling slider (fit-to-one-page, fit-to-width, custom%), and draggable page break indicators; printing uses the configured layout |
| Failed End Condition  | User cancels print setup; no print occurs                                                                                                                                                     |
| Actors                | Primary: Mind Map Author                                                                                                                                                                      |
| Trigger               | User selects File → Print Preview (enhanced) or File → Page Setup                                                                                                                             |

---

## 13. Reliability & Error Handling

### Use Case 36: Receive Notification When Auto-Save Fails

| Field                 | Description                                                                                                                                                                                |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Be immediately and clearly informed when an auto-save operation fails, rather than having the failure silently logged                                                                      |
| Scope & Level         | System / Subfunction                                                                                                                                                                       |
| Preconditions         | Auto-save is enabled; the auto-save target directory is full, read-only, or otherwise inaccessible                                                                                         |
| Success End Condition | A non-modal notification banner appears at the top of the window stating "Auto-save failed: [reason]. Your changes are not backed up." with a "Retry Now" button and a "Save As..." button |
| Failed End Condition  | N/A (the notification itself cannot fail — worst case, the status bar icon turns red)                                                                                                      |
| Actors                | Primary: Mind Map Author                                                                                                                                                                   |
| Trigger               | The auto-save timer fires and the save operation throws an exception                                                                                                                       |

### Use Case 37: Handle Paste with No Node Selected Gracefully

| Field                 | Description                                                                                                                         |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Prevent a NullPointerException crash when the user triggers paste (Ctrl+V) with no node selected, instead showing a helpful message |
| Scope & Level         | System / Subfunction                                                                                                                |
| Preconditions         | No node is selected (user clicked empty canvas); the clipboard contains content                                                     |
| Success End Condition | A status bar message says "Select a node before pasting" or the paste is applied to the root node by default                        |
| Failed End Condition  | N/A (the crash is prevented in all cases)                                                                                           |
| Actors                | Primary: Mind Map Author                                                                                                            |
| Trigger               | User presses Ctrl+V with no node selected                                                                                           |

### Use Case 38: Prevent Resource Leaks on Save Failure

| Field                 | Description                                                                                                                                                            |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Ensure file output streams, buffered writers, and semaphore readers are always closed (via try-with-resources) even when serialisation or I/O errors occur during save |
| Scope & Level         | System / Subfunction                                                                                                                                                   |
| Preconditions         | The user saves a map; an error occurs during XML serialisation                                                                                                         |
| Success End Condition | All I/O resources are closed; no file handle leak occurs; the user sees an error dialog and can retry                                                                  |
| Failed End Condition  | N/A (the fix is unconditional resource cleanup)                                                                                                                        |
| Actors                | Primary: Mind Map Author (indirectly — user benefits from system stability)                                                                                            |
| Trigger               | Any save operation that encounters an error                                                                                                                            |

### Use Case 39: Fix JoinNodes HTML Corruption

| Field                 | Description                                                                                                                                      |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Correctly merge the content of rich-text (HTML) nodes when joining, handling `<body>` tags with attributes and avoiding malformed HTML fragments |
| Scope & Level         | System / Subfunction                                                                                                                             |
| Preconditions         | Multiple sibling nodes with HTML content are selected                                                                                            |
| Success End Condition | The joined node contains well-formed HTML that combines all source node content; an HTML parser (not regex) is used for the merge                |
| Failed End Condition  | N/A (the fix replaces the broken regex approach)                                                                                                 |
| Actors                | Primary: Mind Map Author                                                                                                                         |
| Trigger               | User selects multiple rich-text nodes and invokes Edit → Join Nodes (Ctrl+J)                                                                     |

### Use Case 40: Surface Meaningful Error Messages Instead of Swallowing Exceptions

| Field                 | Description                                                                                                                                                                                                       |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Replace the 77+ empty catch blocks across the codebase with proper error handling: log with context, notify the user when the failure affects their workflow, and never silently discard data                     |
| Scope & Level         | System / Summary                                                                                                                                                                                                  |
| Preconditions         | Any operation that can fail (encryption, file I/O, plugin loading, IPC)                                                                                                                                           |
| Success End Condition | Every catch block either: (a) handles the exception and recovers, (b) logs it with full context and shows a user-facing notification for user-impacting failures, or (c) re-throws if the caller should handle it |
| Failed End Condition  | N/A (code quality improvement)                                                                                                                                                                                    |
| Actors                | Primary: Mind Map Author; Supporting: Developer/Maintainer                                                                                                                                                        |
| Trigger               | Any runtime exception in a currently-empty catch block                                                                                                                                                            |

### Use Case 41: Close EditServer Connections Properly

| Field                 | Description                                                                                                                                                         |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Fix the EditServer's commented-out `finally` block so that client socket connections are closed after handling each request, preventing connection exhaustion       |
| Scope & Level         | System / Subfunction                                                                                                                                                |
| Preconditions         | FreeMind is running with the EditServer listening for IPC                                                                                                           |
| Success End Condition | Each client connection is closed after processing, regardless of success or failure; the server can handle unlimited sequential connections without port exhaustion |
| Failed End Condition  | N/A (resource leak is eliminated)                                                                                                                                   |
| Actors                | Primary: External Application (IPC client); Supporting: FreeMind EditServer                                                                                         |
| Trigger               | Any IPC client connects to the EditServer                                                                                                                           |

---

## 14. Advanced Features

### Use Case 42: Insert a Node from a Template via Quick-Action

| Field                 | Description                                                                                                                                                               |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Quickly insert commonly used node structures (single nodes with pre-set icons, multi-level templates) from a searchable quick-action menu without navigating nested menus |
| Scope & Level         | System / User-goal                                                                                                                                                        |
| Preconditions         | At least one node template is saved or a built-in default exists; a node is selected                                                                                      |
| Success End Condition | The template subtree is inserted as children of the selected node; the first editable text field in the template is focused for immediate customisation                   |
| Failed End Condition  | User cancels the template selector                                                                                                                                        |
| Actors                | Primary: Mind Map Author                                                                                                                                                  |
| Trigger               | User presses a configurable shortcut or selects Insert → Quick Template                                                                                                   |

### Use Case 43: View In-App Documentation for the Scripting API

| Field                 | Description                                                                                                                                                                           |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Access a built-in reference of available Groovy scripting API methods, properties, and examples directly within the Script Editor panel                                               |
| Scope & Level         | System / User-goal                                                                                                                                                                    |
| Preconditions         | The Scripting plugin is loaded; the Script Editor is open                                                                                                                             |
| Success End Condition | A side panel or tab within the Script Editor shows categorised API documentation (node manipulation, map traversal, attribute access) with searchable content and copy-paste examples |
| Failed End Condition  | Documentation fails to load; a link to external documentation is shown as fallback                                                                                                    |
| Actors                | Primary: Power User / Scripter                                                                                                                                                        |
| Trigger               | User clicks "API Reference" in the Script Editor or presses F1 within the editor                                                                                                      |

### Use Case 44: Display Tooltip Documentation for Filter Conditions

| Field                 | Description                                                                                                                                                         |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Understand what each filter condition type (text match, icon contains, attribute equals, etc.) does and how Boolean operators combine them, via in-dialog help text |
| Scope & Level         | System / Subfunction                                                                                                                                                |
| Preconditions         | The Filter Composer dialog is open                                                                                                                                  |
| Success End Condition | Each condition type dropdown item has a tooltip explaining its behaviour; a "?" help button shows a short guide to building filters with AND/OR/NOT logic           |
| Failed End Condition  | N/A (static help content)                                                                                                                                           |
| Actors                | Primary: Mind Map Author                                                                                                                                            |
| Trigger               | User hovers over a condition type or clicks the help button in the Filter Composer                                                                                  |

### Use Case 45: Implement the Underlined Text Formatting Action

| Field                 | Description                                                                                                                                                  |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Make the existing but non-functional `UnderlinedAction` actually render underlined text on nodes in the map view                                             |
| Scope & Level         | System / Subfunction                                                                                                                                         |
| Preconditions         | A node is selected                                                                                                                                           |
| Success End Condition | Toggling underline (via menu or shortcut) causes the node's text to render with an underline; the underline is visible in the map, exports, and print output |
| Failed End Condition  | N/A (implementation completes the existing stub)                                                                                                             |
| Actors                | Primary: Mind Map Author                                                                                                                                     |
| Trigger               | User selects Format → Underline or presses the shortcut                                                                                                      |

### Use Case 46: Provide Contextual Tooltips for Undocumented Features

| Field                 | Description                                                                                                                                                                                                                              |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Goal                  | Add descriptive tooltips to features that currently have no in-UI explanation: Cloud toggle, File Mode, Encryption strength, Arrow Link creation workflow, Node Attributes, Ctrl+Drag vGap behaviour, and F1–F9 pattern keys             |
| Scope & Level         | System / Summary                                                                                                                                                                                                                         |
| Preconditions         | FreeMind is running                                                                                                                                                                                                                      |
| Success End Condition | Hovering over any menu item, toolbar button, or dialog element shows a tooltip that explains what the feature does and, for complex features, how to use it (e.g., "Ctrl+L: Select two nodes first, then press to create an arrow link") |
| Failed End Condition  | N/A (static content addition)                                                                                                                                                                                                            |
| Actors                | Primary: Mind Map Author                                                                                                                                                                                                                 |
| Trigger               | User hovers over any UI element                                                                                                                                                                                                          |

### Use Case 47: Show Encryption Strength Information During Password Entry

| Field                 | Description                                                                                                                                                                          |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Goal                  | Display the encryption algorithm, key length, and a password strength meter in the Enter Password dialog so users can make informed decisions about their data security              |
| Scope & Level         | System / Subfunction                                                                                                                                                                 |
| Preconditions         | The user is creating or unlocking an encrypted node                                                                                                                                  |
| Success End Condition | The dialog shows: "Encryption: AES-256-GCM" (or "DES [INSECURE — upgrade recommended]" for legacy nodes), a password strength bar (weak/fair/strong), and minimum length requirement |
| Failed End Condition  | N/A (informational display)                                                                                                                                                          |
| Actors                | Primary: Mind Map Author                                                                                                                                                             |
| Trigger               | User opens the Enter Password dialog for encryption or decryption                                                                                                                    |

---

## Appendix: Actor Glossary

| Actor                      | Description                                                                                        |
| -------------------------- | -------------------------------------------------------------------------------------------------- |
| **Mind Map Author**        | The primary user who creates, edits, and manages mind maps. Most use cases serve this actor.       |
| **First-Time User**        | A new user encountering FreeMind for the first time, needing guidance and discoverability.         |
| **Presenter**              | A user who uses the mind map as a presentation tool in meetings or talks.                          |
| **Power User / Scripter**  | An advanced user who extends FreeMind via Groovy scripts or custom XSLT.                           |
| **Visually Impaired User** | A user relying on screen readers, high-contrast themes, or other accessibility aids.               |
| **Session Host**           | A user who initiates a collaborative editing session, making their map available to others.        |
| **Remote Collaborator**    | A user who joins an existing collaborative session to co-edit a shared map.                        |
| **External Application**   | A non-human actor (script, IDE plugin) communicating with FreeMind via EditServer IPC.             |
| **Developer/Maintainer**   | A contributor to the FreeMind codebase who benefits from improved error handling and code quality. |

---

## Appendix: Traceability to UX Audit

| Use Case                       | UX Audit Reference                          |
| ------------------------------ | ------------------------------------------- |
| UC-1 (Tutorial)                | Q4.1 — Interactive First-Run Tutorial       |
| UC-2 (Command Palette)         | Q4.4 — Searchable Command Palette           |
| UC-3 (Shortcut Overlay)        | Q4.11, Q3.1–Q3.4 — Undiscoverable shortcuts |
| UC-4 (Auto-Wrap Editor)        | Q1.1 — No Text Wrapping                     |
| UC-5 (Find & Replace)          | Q1.2, Q2.8 — No Replace, Placeholder        |
| UC-6 (Suggestions)             | Q4.14 — Smart Node Suggestions              |
| UC-8 (Live Filter Preview)     | Q1.15 — No Visual Feedback on Filters       |
| UC-9 (Auto-Save Status)        | Q4.10 — Auto-Save Status Indicator          |
| UC-10 (Rich/Plain Indicator)   | Q3.5 — No Visual Indicator of Mode          |
| UC-11 (Undo History)           | Q1.7 — No Visual History                    |
| UC-12 (Link Overview)          | Q4.9 — Node Linking Visualization           |
| UC-13, UC-14 (Markdown)        | Q4.5 — Markdown Import/Export               |
| UC-15 (Export Preview)         | Q1.8 — No Live Preview                      |
| UC-16 (Remove Placeholders)    | Q2.8–Q2.10 — Non-Functional Placeholders    |
| UC-17 (Tab Bar)                | Q1.9, Q4.8 — No Visual Tab Bar              |
| UC-18 (Snap-to-Grid)           | Q1.10 — No Alignment Guides                 |
| UC-19 (Zoom Entry)             | Q1.6 — No Direct Percentage Entry           |
| UC-20 (Version History)        | Q4.15 — Version History / Map Diffing       |
| UC-21 (Presentation Mode)      | Q4.12 — Presentation / Slideshow Mode       |
| UC-22 (Branch Formatting)      | Q1.5 — Edge Formatting Per Node             |
| UC-23 (Colour Palette)         | Q1.11 — No Recent Colors                    |
| UC-24 (Style Preview)          | Q3.12 — No Visual Preview of Styles         |
| UC-25 (Templates)              | Q4.6 — Node Templates                       |
| UC-26 (Icon Search)            | Q1.3 — No Search in Icon Grid               |
| UC-27, UC-28 (Encryption)      | Q2.6, Q4.7 — DES Broken, AES Needed         |
| UC-29, UC-30 (Collaboration)   | Q1.13, Q4.2 — Complex Setup, No Presence    |
| UC-31, UC-32 (Accessibility)   | Q4.13 — No Accessibility Support            |
| UC-33 (Search Preferences)     | Q1.4 — Overwhelming Preferences             |
| UC-34 (Dark Mode)              | Q4.3 — Dark Mode / Theme System             |
| UC-35 (Print Layout)           | Q1.12 — No Page Layout Control              |
| UC-36 (Auto-Save Notification) | Q2.12 — Silent Auto-Save Failure            |
| UC-37 (Paste Null Check)       | Q2.2 — NullPointerException on Paste        |
| UC-38 (Resource Leaks)         | Q2.3, Q2.4 — File Handle Leaks              |
| UC-39 (Join HTML Fix)          | Q2.5 — JoinNodes Corrupts HTML              |
| UC-40 (Exception Handling)     | Q2.1 — 77+ Empty Catch Blocks               |
| UC-41 (EditServer Fix)         | Q2.11 — Socket Connection Leak              |
| UC-43 (Script Docs)            | Q3.11 — No In-App Script Documentation      |
| UC-44 (Filter Help)            | Q3.14 — No Filter Logic Explanation         |
| UC-45 (Underline Fix)          | Q2.7 — Underlined Action Non-Functional     |
| UC-46 (Tooltips)               | Q3.6–Q3.10 — Multiple Undocumented Features |
| UC-47 (Encryption Info)        | Q3.10 — No Encryption Documentation         |
