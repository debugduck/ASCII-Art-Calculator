import java.util.*;

public class DigitList {
    protected Node high; // reference to the node with the high-order digit
    protected Node low;  // reference to the node with the low-order digit
    protected int size;  // number of nodes
    protected int modCount;

    // Workhorse constructor. Initialize variables.
    DigitList() {
        high = null;
        low = null;
        size = 0;
        modCount = 0;
    }

    // helper method to increment size and modcount
    // because I'm lazy and I don't like looking
    // at a bunch of lines of incrementing variables
    // in every method
    private void incrementStuff() {
        size++;
        modCount++;
    }

    // same tbh
    private void decrementStuff() {
        size--;
        modCount++;
    }

    // Add digit x to the front of the list.
    // Target Complexity: O(1)
    public void addFirst(int x) {
        Node toAdd = new Node(x, null, high);
        if(high == null) {
            low = toAdd;
        }
        if(high != null) {
            Node temp = high;
            temp.prev = toAdd;
        }
        high = toAdd;
        incrementStuff();
    }

    protected int sumDigits() {
        Node current = this.high;
        int sum = 0;
        while(current != null) {
            sum += current.data;
            current = current.next;
        }
        return sum;
    }

    // Returns digit high (at the front of the list).
    // Target Complexity: O(1)
    public int getFirst() {
        return high.data;
    }

    // Removes digit high at the front of the list.
    // Target Complexity: O(1)
    public void removeFirst() {
        if(size > 1) {
            high = high.next;
            decrementStuff();
        }
    }

    // Add digit x to the end of the list.
    // Target Complexity: O(1)
    public void addLast(int x) {
        Node toAdd = new Node(x, low, null);
        if(low == null) {
            low = toAdd;
        }
        else {
            low.next = toAdd;
        }
        if(high == null) {
            high = low;
        }
        low = toAdd;
        incrementStuff();
    }

    // Returns digit low (at the end of the list).
    // Target Complexity: O(1)
    public int getLast() {
        return low.data;
    }

    // Removes digit low at the end of the list.
    // Target Complexity: O(1)
    public void removeLast() {
        if(size > 1) {
            low = low.prev;
            low.next = null;
            decrementStuff();
        }
    }

    // Returns a pretty representation of the digits in the list.
    // Example: 3.1416
    // O(number of digits)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = high;
        for( ; current.next != null; current = current.next) {
            sb.append(current.data);
        }
        sb.append(current.data);
        return sb.toString();
    }

    // Returns the current size of the list
    // Target Complexity: O(1)
    public int size() {
        return size;
    }

    // Obtains a ListIterator object used to traverse the List
    // bidirectionally.
    // Returns an iterator positioned prior to the first element
    public ListIterator<Integer> listIterator( ){
        return new DigitListIterator( 0 );
    }

    // Obtains a ListIterator object used to traverse the list
    // bidirectionally.
    // Returns an iterator positioned prior to the requested element.
    // Parameter idx is the index to start the iterator. Use size()
    // to do complete reverse traversal. Use 0 to do a complete forward
    // traversal.
    // Throws IndexOutOfBoundsException if idx is not between 0
    // and size(), inclusive.
    public ListIterator<Integer> listIterator( int idx ){
        return new DigitListIterator( idx );
    }


    // This class implements the ListIterator interface for the
    // DigitList.  It maintains a notion of a current position and
    // implicit reference to the DigitList through the syntax
    // DigitList.this.
    public class DigitListIterator implements ListIterator<Integer>{
        // Current node
        protected Node current;
        // Necessary for implementing previous()
        protected boolean lastMoveWasPrev = false;
        // How many modifications iterator expects
        protected int expectedModCount = modCount;
        // the index of the current Node
        protected int currentIndex = 0;

        // Construct an iterator
        public DigitListIterator( int idx ) {
            current = high;
            while(currentIndex < idx) {
                current = current.next;
                currentIndex++;
            }
        }

        // Returns true if this list iterator has more elements when
        // traversing the list in the forward direction. (In other words,
        // returns true if next() would return an element rather than
        // throwing an exception.)
        public boolean hasNext( ) {
            return current.next != null;
        }

        // Returns the current element in the list and advances the cursor
        // position.
        // Throws: NoSuchElementException if the iteration has no next
        // element.
        public Integer next( ) {
            //if(!hasNext()) throw new NoSuchElementException();
            Integer currentData = current.data;
            if(hasNext()) {
                current = current.next;
            }
            return currentData;
        }

        // Returns true if this list iterator has more elements when
        // traversing the list in the reverse direction.
        public boolean hasPrevious( ) {
            return current.prev != null;
        }

        // Returns the current element in the list and moves the cursor
        // position backwards. This method may be called repeatedly to
        // iterate through the list backwards, or intermixed with calls to
        // next() to go back and forth. (Note that alternating calls to
        // next and previous will return the same element repeatedly.)
        // Throws: NoSuchElementException if the iteration has no next
        // element.
        public Integer previous( ) {
            //if(!hasPrevious()) throw new NoSuchElementException();
            Integer currentData = current.data;
            if(hasPrevious()) {
                current = current.prev;
            }
            return currentData;
        }

        // The following methods may be optionally implemented but will
        // not be tested and will not garner any additional credit.
        //
        // OPTIONAL: Return the integer index associated with the element
        // that would be returned by next()
        public int nextIndex(){
            int result = currentIndex + 1;
            assert result <= size;
            return result;
        }
        // OPTIONAL: Return the integer index associated with the element
        // that would be returned by previous()
        public int previousIndex(){
            int result = currentIndex - 1;
            assert result > -1;
            return result;
        }
        // OPTIONAL: Inserts the specified element into the list. The element
        // is inserted immediately before the element that would be returned
        // by next(), if any, and after the element that would be returned by
        // previous(), if any. (If the list contains no elements, the new
        // element becomes the sole element on the list.) The new element is
        // inserted before the implicit cursor: a subsequent call to next
        // would be unaffected, and a subsequent call to previous would
        // return the new element. (This call increases by one the value that
        // would be returned by a call to nextIndex or previousIndex.)
        public void add(Integer x) {
            throw new RuntimeException("Implement me for fun");
        }

        // The following operations are part of the ListIterator interface
        // but are not supported by DigitLists or their iterators. Both
        // will throw UnsupportedOperationException exceptions if invoked.
        public void set(Integer x){
            throw new UnsupportedOperationException();
        }
        public void remove( ) {
            throw new UnsupportedOperationException();
        }
    }
}