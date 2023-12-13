package ru.practicum.android.diploma.vacancy.ui.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.domain.repository.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun openMail(mailTo: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(
                Intent.EXTRA_EMAIL, arrayOf(mailTo)
            )
        }
        startActivityOrShowError(intent)
    }

    override fun callPhone(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val chooser = Intent.createChooser(intent, null)
        startActivityOrShowError(chooser)
    }

    override fun shareVacancyById(id: Int) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT, "${context.getString(R.string.link_to_hh_ru)}$id"
            )
        }
        startActivityOrShowError(intent)
    }

    override fun openAppsSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                PACKAGE, context.packageName, null
            )
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun startActivityOrShowError(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: Throwable) {
            Toast.makeText(
                context,
                context.getString(R.string.there_is_no_app_on_the_device_to_make_this_request),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val PACKAGE = "package"
    }
}