package com.ivanasen.smarttickets.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import com.google.android.gms.location.places.Place
import com.ivanasen.smarttickets.db.models.Event
import com.ivanasen.smarttickets.db.models.Ticket
import com.ivanasen.smarttickets.db.models.TicketType
import com.ivanasen.smarttickets.repositories.SmartTicketsRepository
import com.ivanasen.smarttickets.util.Utility
import org.jetbrains.anko.defaultSharedPreferences
import java.io.File
import java.math.BigDecimal


class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = SmartTicketsRepository
    private val WALLET_FILE_NAME = "WalletFileName"
    private val MIN_PASSWORD_LENGTH = 8

    val credentials = mRepository.credentials


    var contractExists = mRepository.contractDeployed

    val etherBalance: LiveData<BigDecimal> = mRepository.etherBalance
    val usdBalance: LiveData<Double> = mRepository.usdBalance

    val myEvents: LiveData<MutableList<Event>> = mRepository.myEvents
    val events: LiveData<MutableList<Event>> = mRepository.events
    val tickets: LiveData<MutableList<Ticket>> = mRepository.tickets

//    public fun refreshEvents(): LiveData<List<IPFSEvent>> {
//
//    }
//
//    public fun getTicketsForUser(): LiveData<List<TicketType>> {
//
//    }

    fun unlockWallet(password: String, context: Context): Boolean {
        val appContext = context.applicationContext
        val walletName = appContext.defaultSharedPreferences.getString(WALLET_FILE_NAME, "")
        require(isThereAWallet(context))

        val wallet = File(appContext.filesDir, walletName)
        return mRepository.unlockWallet(password, wallet)
    }

    fun createNewWallet(password: String, context: Context) {
        require(password.length < MIN_PASSWORD_LENGTH)
        val walletName = mRepository.createWallet(password, context.filesDir)
    }

    fun isThereAWallet(context: Context): Boolean {
        val appContext = context.applicationContext
        return try {
            require(appContext.defaultSharedPreferences.getString(WALLET_FILE_NAME, "") != "")
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun sendEther(address: String, amount: Double) {
        mRepository.sendEtherTo(address, amount)
    }

    fun addEvent(context: Context) {

    }

    fun onPlacePicked(place: Place?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun refreshEvents() {
        mRepository.fetchEvents()
    }

    fun fetchEvent(eventId: Long): LiveData<Event> {
        return mRepository.fetchEvent(eventId)
    }

    fun fetchTicketTypesForEvent(eventId: Long): LiveData<MutableList<TicketType>> {
        return mRepository.fetchTicketTypesForEvent(eventId)
    }

    fun attemptToBuyTicket(ticketType: TicketType): LiveData<Utility.Companion.TransactionStatus> {
        return mRepository.buyTicket(ticketType)
    }

    fun fetchBalance() {
        mRepository.fetchBalance()
    }

    fun fetchMyEvents() {
        mRepository.fetchMyEvents()
    }

    fun refreshTickets() {
        return mRepository.fetchTickets()
    }

    fun loadInitialAppData() {
        mRepository.loadInitialAppData()
    }
}