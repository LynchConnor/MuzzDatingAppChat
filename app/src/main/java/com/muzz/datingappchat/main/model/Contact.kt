package com.muzz.datingappchat.main.model

enum class Contact {
    Connor, Muzz;

    val id: String
        get() = when (this) {
            Contact.Connor -> "id-1"
            Contact.Muzz -> "id-2"
        }

    fun toggle(): Contact {
        return when(this) {
            Contact.Connor -> Muzz
            Contact.Muzz -> Connor
        }
    }
}