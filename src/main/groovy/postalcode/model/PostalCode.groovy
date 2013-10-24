package postalcode.model

/**
 * Immutable object representing a postal code
 */
class PostalCode {

    /**
     * Postal code
     */
    final String code

    /**
     * State for which the postal code belongs
     */
    final String state

    /**
     * Abbreviations for the state
     */
    final String abbr

    public PostalCode(String code, String state, String abbr) {
        this.code = code
        this.state = state
        this.abbr = abbr
    }

    @Override
    public int hashCode() {
        int hash = 3
        hash = 7 * hash + this.code.hashCode()
        hash = 7 * hash + this.state.hashCode()
        hash = 7 * hash + this.abbr.hashCode()
        return hash
    }

    @Override
    public boolean equals(Object obj) {
        final PostalCode that = obj as PostalCode
        if(that == null) {
            return false
        } else {
            return (this.code == that.code && this.state == that.state && this.abbr == that.abbr)
        }
    }

    @Override
    public String toString() {
        return "$code $state ($abbr)"
    }
}
