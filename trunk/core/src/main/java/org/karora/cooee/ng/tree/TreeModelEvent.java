package org.karora.cooee.ng.tree;

/* 
 * This file is part of the Echo Point Project.  This project is a collection
 * of Components that have extended the Echo Web Application Framework.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

 /*
 * The design paradigm and class name used within have been taken directly from
 * the java.swing package has been retro-fitted to work with the NextApp Echo web framework.
 *
 * This file was made part of the EchoPoint project on the 25/07/2002.
 *
 */

import java.util.EventObject;

/**
 * Encapsulates information describing changes to a tree model, and
 * used to notify tree model listeners of the change.
 * <p>
 */
public class TreeModelEvent extends EventObject {
    /** Path to the parent of the nodes that have changed. */
    protected TreePath path;
    /** Indices identifying the position of where the children were. */
    protected int[] childIndices;
    /** Children that have been removed. */
    protected Object[] children;

    /**
     * Used to create an event when the node structure has changed in some way,
     * identifying the path to the root of a modified subtree as an array of
     * Objects. A structure change event might involve nodes swapping position,
     * for example, or it might encapsulate multiple inserts and deletes in the
     * subtree stemming from the node, where the changes may have taken place at
     * different levels of the subtree.
     * <blockquote>
     *   <b>Note:</b><br>
     *   Tree collapses all nodes under the specified node, so that only its
     *   immediate children are visible.
     * </blockquote>
     *
     */
    public TreeModelEvent(Object source, Object[] path) {
        this(source, new TreePath(path));
    }
    /**
     * Used to create an event when nodes have been changed, inserted, or
     * removed, identifying the path to the parent of the modified items as
     * an array of Objects. All of the modified objects are siblings which are
     * direct descendents (not grandchildren) of the specified parent.
     * The positions at which the inserts, deletes, or changes occured are
     * specified by an array of <code>int</code>. The indexes in that array
     * must be in order, from lowest to highest.
     * <p>
     * For changes, the indexes in the model correspond exactly to the indexes
     * of items currently displayed in the UI. As a result, it is not really
     * critical if the indexes are not in their exact order. But after multiple
     * inserts or deletes, the items currently in the UI no longer correspond
     * to the items in the model. It is therefore critical to specify the
     * indexes properly for inserts and deletes.
     * <p>
     * For inserts, the indexes represent the <i>final</i> state of the tree,
     * after the inserts have occurred. Since the indexes must be specified in
     * order, the most natural processing methodology is to do the inserts
     * starting at the lowest index and working towards the highest. Accumulate
     * a Vector of <code>Integer</code> objects that specify the
     * insert-locations as you go, then convert the Vector to an
     * array of <code>int</code> to create the event. When the postition-index
     * equals zero, the node is inserted at the beginning of the list. When the
     * position index equals the size of the list, the node is "inserted" at
     * (appended to) the end of the list.
     * <p>
     * For deletes, the indexes represent the <i>initial</i> state of the tree,
     * before the deletes have occurred. Since the indexes must be specified in
     * order, the most natural processing methodology is to use a delete-counter.
     * Start by initializing the counter to zero and start work through the
     * list from lowest to higest. Every time you do a delete, add the current
     * value of the delete-counter to the index-position where the delete occurred,
     * and append the result to a Vector of delete-locations, using
     * <code>addElement()</code>. Then increment the delete-counter. The index
     * positions stored in the Vector therefore reflect the effects of all previous
     * deletes, so they represent each object's position in the initial tree.
     * (You could also start at the highest index and working back towards the
     * lowest, accumulating a Vector of delete-locations as you go using the
     * <code>insertElementAt(Integer, 0)</code>.) However you produce the Vector
     * of initial-positions, you then need to convert the Vector of <code>Integer</code>
     * objects to an array of <code>int</code> to create the event.
     * <p>
     * <b>Notes:</b><ul>
     * <li>Like the <code>insertNodeInto</code> method in the
     *    <code>DefaultTreeModel</code> class, <code>insertElementAt</code>
     *    appends to the <code>Vector</code> when the index matches the size
     *    of the vector. So you can use <code>insertElementAt(Integer, 0)</code>
     *    even when the vector is empty.
     * <ul>To create a node changed event for the root node, specify the parent
     *     as null and the "child" index as zero.
     * </ul>
     *
     * @param source the Object responsible for generating the event (typically
     *               the creator of the event object passes <code>this</code>
     *               for its value)
     * @param path   an array of Object identifying the path to the
     *               parent of the modified item(s), where the first element
     *               of the array is the Object stored at the root node and
     *               the last element is the Object stored at the parent node
     * @param childIndices an array of <code>int</code> that specifies the
     *               index values of the removed items. The indices must be
     *               in sorted order, from lowest to highest
     * @param children an array of Object containing the inserted, removed, or
     *                 changed objects
     */
    public TreeModelEvent(Object source, Object[] path, int[] childIndices, Object[] children) {
        this(source, new TreePath(path), childIndices, children);
    }
    /**
     * Used to create an event when the node structure has changed in some way,
     * identifying the path to the root of the modified subtree as a TreePath
     * object. For more information on this event specification, see
     * <code>TreeModelEvent(Object,Object[])</code>.
     *
     * @param source the Object responsible for generating the event (typically
     *               the creator of the event object passes <code>this</code>
     *               for its value)
     * @param path   a TreePath object that identifies the path to the
     *               change. In the DefaultTreeModel,
     *               this object contains an array of user-data objects,
     *               but a subclass of TreePath could use some totally
     *               different mechanism -- for example, a node ID number
     *
     */
    public TreeModelEvent(Object source, TreePath path) {
    	this(source, path, null, null);
    }
    /**
     * Used to create an event when nodes have been changed, inserted, or
     * removed, identifying the path to the parent of the modified items as
     * a TreePath object. For more information on how to specify the indexes
     * and objects, see
     * <code>TreeModelEvent(Object,Object[],int[],Object[])</code>.
     *
     * @param source the Object responsible for generating the event (typically
     *               the creator of the event object passes <code>this</code>
     *               for its value)
     * @param path   a TreePath object that identifies the path to the
     *               parent of the modified item(s)
     * @param childIndices an array of <code>int</code> that specifies the
     *               index values of the modified items
     * @param children an array of Object containing the inserted, removed, or
     *                 changed objects
     *
     */
    public TreeModelEvent(Object source, TreePath path, int[] childIndices, Object[] children) {
        super(source);
        this.path = path;
        if (childIndices != null && children != null) {
          this.childIndices = childIndices;
          this.children = children;
        } else {
          this.childIndices = new int[0];
          this.children = new Object[0];
        }
    }
    /**
     * Returns the values of the child indexes. If this is a removal event
     * the indexes point to locations in the initial list where items
     * were removed. If it is an insert, the indices point to locations
     * in the final list where the items were added. For node changes,
     * the indices point to the locations of the modified nodes.
     *
     * @return an array of <code>int</code> containing index locations for
     *         the children specified by the event
     */
    public int[] getChildIndices() {
        if (childIndices != null) {
            int cCount = childIndices.length;
            int[] retArray = new int[cCount];

            System.arraycopy(childIndices, 0, retArray, 0, cCount);
            return retArray;
        }
        return null;
    }
    /**
     * Returns the objects that are children of the node identified by
     * <code>getPath</code> at the locations specified by
     * <code>getChildIndices</code>. If this is a removal event the
     * returned objects are no longer children of the parent node.
     *
     * @return an array of Object containing the children specified by
     *         the event
     */
    public Object[] getChildren() {
        if (children != null) {
            int cCount = children.length;
            Object[] retChildren = new Object[cCount];

            System.arraycopy(children, 0, retChildren, 0, cCount);
            return retChildren;
        }
        return null;
    }
    /**
     * Convenience method to get the array of objects from the TreePath
     * instance that this event wraps.
     *
     * @return an array of Objects, where the first Object is the one
     *         stored at the root and the last object is the one
     *         stored at the node identified by the path
     */
    public Object[] getPath() {
        if (path != null)
            return path.getPath();
        return null;
    }
    /**
     * Returns the TreePath object identifying the changed node.
     * Use <code>getLastPathComponent</code> on that object to
     * get the data stored at that node.
     *
     */
    public TreePath getTreePath() {
        return path;
    }
    /**
     * Returns a string that displays and identifies this object's
     * properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        StringBuffer retBuffer = new StringBuffer();

        retBuffer.append(getClass().getName() + " " + Integer.toString(hashCode()));
        if (path != null)
            retBuffer.append(" path " + path);
        if (childIndices != null) {
            retBuffer.append(" indicices [ ");
            for (int counter = 0; counter < childIndices.length; counter++)
                retBuffer.append(Integer.toString(childIndices[counter]) + " ");
            retBuffer.append("]");
        }
        if (children != null) {
            retBuffer.append(" children [ ");
            for (int counter = 0; counter < children.length; counter++)
                retBuffer.append(children[counter] + " ");
            retBuffer.append("]");
        }
        return retBuffer.toString();
    }
}
