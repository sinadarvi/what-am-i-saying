package what

import kotlinext.js.jsObject
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import kotlin.browser.document
import kotlin.js.Promise

@JsModule("axios")
external fun <T> axios(config: AxiosConfigSettings): Promise<AxiosResponse<Array<T>>>

external interface AxiosConfigSettings {
    var url: String
    var timeout: Number
}

interface AxiosState : RState {
    var statusText: String
    var dataResult: DataResult
    var errorMessage: String
}

external interface AxiosResponse<T> {
    val data: T
}

data class DataResult(var stats: String,
                      var finglish: String,
                      var farsi: Array<String>)

class What : RComponent<RProps, AxiosState>() {

    override fun AxiosState.init() {
        statusText = ""
        dataResult = DataResult("", "", arrayOf(""))
        errorMessage = ""
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    private fun whatAmISaying(text: String) {
        val config: AxiosConfigSettings = jsObject {
            url = "https://inputtools.google.com/request?text=$text&itc=fa-t-i0-und&num=13"
            timeout = 3000
        }

        axios<DataResult>(config).then { response ->
            setState {
                dataResult.apply {
                    stats = response.data[0] as String
                    finglish = ((response.data[1] as Array<String>)[0] as Array<String>)[0]
                    farsi = ((response.data[1] as Array<String>)[0] as Array<String>)[1] as Array<String>
                }
                errorMessage = ""
            }

            console.log(state.dataResult.farsi[0])
        }.catch { error ->
            setState {
                dataResult = DataResult("", "", arrayOf(""))
                errorMessage = error.message ?: ""
            }
            console.log(error)
        }
    }

    override fun RBuilder.render() {
        var inputValue: String
        div("what-am-i-saying") {
            h2 {
                +"WHAT AM I SAYING?"
            }
            div("what") {
                input {
                    attrs {
                        id = "input"
                        type = InputType.text
                        placeholder = "salam jahan!"
                        autoComplete = false
                        required = true
                    }
                }
                button {
                    span {
                        +"What?"
                    }
                    attrs {
                        onClickFunction = {
                            inputValue = (document.getElementById("input") as HTMLInputElement).value
                            if (inputValue.isNotEmpty())
                                whatAmISaying(inputValue)
                        }
                    }
                }
            }
        }

        div("result") {
            +state.dataResult.farsi[0]
        }
    }
}

fun RBuilder.what() = child(What::class) {}