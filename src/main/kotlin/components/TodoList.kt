package components

import kotlinx.coroutines.delay
import kotlinx.css.FontWeight
import kotlinx.css.fontWeight
import kotlinx.html.HtmlBlockTag
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.span
import net.justmachinery.shade.component.Component
import net.justmachinery.shade.component.PropsType
import net.justmachinery.shade.state.observable
import net.justmachinery.shade.utility.key
import net.justmachinery.shade.utility.withStyle

class TodoList : Component<TodoList.Props>() {
    data class Props(val userName: String) : PropsType<Props, TodoList>()

    var todoList by observable(listOf<String>())
    var newItem = observable("")
    override fun HtmlBlockTag.render() {
        p {
            +"Hello, "
            span {
                withStyle {
                    fontWeight = FontWeight.bold
                }
                +props.userName
            }
        }
        todoList.forEach { item ->
            div {
                key = item
                +"TODO: "
                +item
            }
        }
        +"Add a new item:"
        boundInput(newItem) {
            type = InputType.text
        }
        button {
            +"Add"
            onClick {
                delay(1000)

                todoList = todoList + newItem.value
            }
        }
    }
}