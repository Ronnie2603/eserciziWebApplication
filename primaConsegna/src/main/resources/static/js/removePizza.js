function removePizza(button) {
    const row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
}