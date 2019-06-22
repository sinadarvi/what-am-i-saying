package index

import kotlinext.js.*
import react.dom.*
import what.what
import kotlin.browser.*

fun main() {
    requireAll(require.context("src", true, js("/\\.css$/")))

    render(document.getElementById("root")) {
        what()
    }
}
