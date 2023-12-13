package ru.practicum.android.diploma.search.data.network

// родительский класс ответов от сервера
// от него должны наследоваться все классы, описывающие параметры ответа
// содержит поле код ответа сервера
open class Response() {var resultCode = 0}