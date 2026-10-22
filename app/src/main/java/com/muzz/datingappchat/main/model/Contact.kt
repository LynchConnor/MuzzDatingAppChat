package com.muzz.datingappchat.main.model

enum class Contact {
    Connor, Muzz;

    /**
     * Basic [id], keeping track of Connor and Muzz
     */
    val id: String
        get() = when (this) {
            Contact.Connor -> "id-1"
            Contact.Muzz -> "id-2"
        }

    /**
     * Toggling [Contact] returns the other contact
     * Muzz -> Connor, Connor -> Muzz
     */
    fun toggle(): Contact {
        return when(this) {
            Contact.Connor -> Muzz
            Contact.Muzz -> Connor
        }
    }
}