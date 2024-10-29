function addPizza() {
      const table = document.getElementById("pizza-table").getElementsByTagName('tbody')[0];
      const name = document.getElementById("pizzaName").value;
      const ingredients = document.getElementById("pizzaIngredients").value;
      const price = document.getElementById("pizzaPrice").value;

      const row = table.insertRow();
      row.innerHTML = `<td>${name}</td><td>${ingredients}</td><td>€${price}</td><td><button class="btn btn-danger" onclick="removePizza(this)">Elimina</button></td>`;

      document.getElementById("addPizzaForm").reset();
}