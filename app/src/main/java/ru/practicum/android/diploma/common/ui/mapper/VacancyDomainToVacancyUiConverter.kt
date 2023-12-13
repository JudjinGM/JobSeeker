package ru.practicum.android.diploma.common.ui.mapper

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Contacts
import ru.practicum.android.diploma.common.domain.model.vacancy_models.KeySkill
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Salary
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Vacancy
import ru.practicum.android.diploma.common.ui.model.PhoneUi
import ru.practicum.android.diploma.common.ui.model.VacancyUi
import java.util.Locale

class VacancyDomainToVacancyUiConverter(private val context: Context) {
    fun mapVacancyToVacancyUi(vacancy: Vacancy): VacancyUi {
        vacancy.apply {
            return VacancyUi(
                id = id,
                name = name,
                areaName = area?.name ?: "",
                employerLogoUrl240 = employer?.logoUrls?.logo240 ?: "",
                employerLogoUrl90 = employer?.logoUrls?.logo90 ?: "",
                employerLogoUrlOriginal = employer?.logoUrls?.original ?: "",
                employerName = employer?.name ?: "",
                salaryAmount = getSalary(
                    mapSalaryDigitFrom(salary),
                    mapSalaryDigitTo(salary),
                    mapSalaryCurrency(salary)
                ),
                experienceName = experience?.name ?: "",
                employmentName = employment?.name ?: "",
                description = description ?: "",
                keySkills = mapKeySkills(keySkills),
                scheduleName = schedule?.name ?: "",
                contactsEmail = contacts?.email ?: "",
                contactsName = contacts?.name ?: "",
                contactsPhones = mapContactPhone(contacts)
            )
        }
    }

    private fun mapSalaryCurrency(salary: Salary?): String {
        return when {
            salary?.currency?.contains("AZN") == true -> {context.getString(R.string.AZN)}
            salary?.currency?.contains("BYR") == true -> {context.getString(R.string.BYR)}
            salary?.currency?.contains("EUR") == true -> {context.getString(R.string.EUR)}
            salary?.currency?.contains("GEL") == true -> {context.getString(R.string.GEL)}
            salary?.currency?.contains("KGS") == true -> {context.getString(R.string.KGT)}
            salary?.currency?.contains("KZT") == true -> {context.getString(R.string.KZT)}
            salary?.currency?.contains("RUR") == true -> {context.getString(R.string.RUB)}
            salary?.currency?.contains("UAH") == true -> {context.getString(R.string.UAH)}
            salary?.currency?.contains("USD") == true -> {context.getString(R.string.USD)}
            salary?.currency?.contains("UZS") == true -> {context.getString(R.string.UZS)}
            else -> ""
            }
    }

    private fun mapSalaryDigitFrom(salary: Salary?): String {
        return if (salary?.from == null) ""
        else mapSalaryDigit(salary.from)
    }

    private fun mapSalaryDigitTo(salary: Salary?): String {
        return if (salary?.to == null) ""
        else mapSalaryDigit(salary.to)
    }

    private fun mapSalaryDigit(amount: Int): String {
        val dec =
            java.text.DecimalFormat("#,###,###,###", java.text.DecimalFormatSymbols(Locale.ENGLISH))

        val result = try {
            dec.format(amount).replace(",", " ")
        } catch (e: Exception) {
            ""
        }
        return result
    }

    private fun getSalary(salaryFrom: String, salaryTo: String, currency: String): String {
        var result = buildString {
            if (salaryFrom.isNotBlank()) {
                append("${context.getString(R.string.from)} $salaryFrom $currency ")
            }
            if (salaryTo.isNotBlank()) {
                append("${context.getString(R.string.to)} $salaryTo $currency")
            }
        }
        if (result.isBlank()) result = context.getString(R.string.no_salary)

        return result
    }

    private fun mapKeySkills(keySkills: List<KeySkill>?): List<String> {
        if (keySkills == null) return emptyList()
        return keySkills.map { keySkill -> keySkill.name }
    }

    private fun mapContactPhone(contacts: Contacts?): List<PhoneUi> {
        return if (contacts?.phones == null) emptyList()
        else {
            contacts.phones.mapIndexed { position, phone ->
                val phoneNumber = buildString {
                    append("+${phone.country} (${phone.city}) ${mapPhoneNumber(phone.number)}")
                }
                PhoneUi(
                    id = position,
                    formattedNumber = phoneNumber,
                    city = phone.city,
                    comment = phone.comment ?: "",
                    country = phone.country,
                    number = phone.number
                )
            }
        }
    }

    private fun mapPhoneNumber(number: String?): String {
        return when (number?.length) {
            0 -> { "" }
            3, 4 -> { String.format("%s-%s", number.dropLast(2), number.takeLast(2)) }
            5, 6, 7, 8 -> {
                String.format(
                    "%s-%s-%s",
                    number.dropLast(4),
                    number.takeLast(4).dropLast(2),
                    number.takeLast(2)
                )
            }
            else -> { "$number" }
        }
    }
}
