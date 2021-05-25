package nl.avwie.declarative

object Group : Node.Descriptor<Group.Builder>({ Builder() }) {
    class Builder : Node.Builder(Group)
}

object Rectangle : Node.Descriptor<Rectangle.Builder>({ Builder() }) {
    class Builder : Node.Builder(Rectangle) {
        var width by attribute(Width, 0)
        var height by attribute(Height, 0)
        var color by attribute(Color, "#000000")
    }
}

fun Node.Builder.Rectangle(width: Int, height: Int, block: Rectangle.Builder.() -> Unit) {
    Rectangle {
        this.width = width
        this.height = height
        block(this)
    }
}