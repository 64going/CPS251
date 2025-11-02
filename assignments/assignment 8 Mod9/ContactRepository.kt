package com.example.test


class ContactRepository(private val contactDao: ContactDao) {
    suspend fun insert(contact: Contact) = contactDao.insert(contact)
    suspend fun delete(contact: Contact) = contactDao.delete(contact)
    fun findContacts(contactName: String) = contactDao.findContacts(contactName)
    fun getContactsSortedByNameDesc() = contactDao.getContactsSortedByNameDesc()
    fun getContactsSortedByNameAsc() = contactDao.getContactsSortedByNameAsc()
}