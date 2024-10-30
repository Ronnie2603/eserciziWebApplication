document.getElementById("addPizzaForm").addEventListener("submit", function(event) {
      event.preventDefault();

      const name = document.getElementById("pizzaName").value;
      const ingredients = document.getElementById("pizzaIngredients").value;
      const price = document.getElementById("pizzaPrice").value;

      const table = document.getElementById("pizza-table").getElementsByTagName('tbody')[0];
  
      addPizza(table, name, ingredients, price);
  
      document.getElementById("addPizzaForm").reset();
});


function addPizza(table, name, ingredients, price) {
      const row = table.insertRow();
      row.innerHTML = `<td>${name}</td>
                       <td>${ingredients}</td>
                       <td>€${price}</td>
                       <td><button class="btn btn-danger" onclick="removePizza(this)">Elimina</button></td>`;
}