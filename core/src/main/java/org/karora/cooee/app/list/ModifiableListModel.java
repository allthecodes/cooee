package org.karora.cooee.app.list;

/**
 * 
 * Interface for creating list model that permit changes to it.
 * 
 * @author gmccreath
 * @since 1.2
 *
 */
public interface ModifiableListModel extends ListModel {

	/**
	 * Adds an item at the end of the model.
	 *
	 * @param item the item to add
	 */
	public void add(Object item);

	/**
	 * Inserts an item at the specified index.
	 *
	 * @param item the item
	 * @param index the index
	 */
	public void add(int index, Object item);

	/**
	 * Returns the item at the specified index in the list.
	 *
	 * @param index 
	 * @return the item
	 */
	public Object get(int index);

	/**
	 * Returns the index of the specified item.
	 *
	 * @param item the item
	 * @return the index
	 */
	public int indexOf(Object item);

	/**
	 * Removes the item at the specified index from the model.
	 *
	 * @param index the index
	 */
	public void remove(int index);

	/**
	 * Removes the specified item from the model.
	 *
	 * @param item the item
	 */
	public void remove(Object item);

	/**
	 * Removes all items from the model.
	 */
	public void removeAll();

}