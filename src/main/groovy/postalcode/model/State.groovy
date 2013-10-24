package postalcode.model

/**
 * Immutable object representing a state
 */
class State {

    /**
     * State name
     */
    final String name

    /**
     * Postal code count
     */
    final int count

    State(String name, int count) {
        this.name = name
        this.count = count
    }

    @Override
    public int hashCode() {
        int hash = 3
        hash = 7 * hash + this.name.hashCode()
        hash = 7 * hash + this.count.hashCode()
        return hash
    }

    @Override
    public boolean equals(Object obj) {
        final State that = obj as State
        if(that == null) {
            return false
        } else {
            return (this.name == that.name && this.count == that.count)
        }
    }

    @Override
    public String toString() {
        return "$name ($count)"
    }

}
