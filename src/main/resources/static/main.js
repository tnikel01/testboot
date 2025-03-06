const btn12 = document.getElementById('btn12');
const btn21 = document.getElementById('btn21');
const btnadd = document.getElementById('btnadd');
const addSpan = document.getElementById('addSpan');
const addEn = document.getElementById('addEn');
const Fieldload = document.getElementById('load');
const Useranswer = document.getElementById('Useranswer');
const messageContainer = document.getElementById('learndiv');

btn12.addEventListener('click', function() {
  const newMessage = document.createElement('p');

  btn12.style.display = "none";
  btn21.style.display = "none";

  messageContainer.style.display = "block";
  newMessage.textContent = 'English-Spanish';

  messageContainer.appendChild(newMessage);
  getEnglishWord();
});

btn21.addEventListener('click', function() {
  const newMessage = document.createElement('p');
  btn12.style.display = "none";
  btn21.style.display = "none";

  messageContainer.style.display = "block";
  newMessage.textContent = 'Spanish-English';

  messageContainer.appendChild(newMessage);
  getSpanishWord();
});

btnadd.addEventListener('click', function(){
  fetch('https://orange-space-train-x5qjv4w75vqhv47x-8080.app.github.dev/api/addWord', {
    method: "POST",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify({ message: addEn.value })
  })
  .then(response => response.text())
  .then(data => console.log("Response from server:", data))
  .catch(error => console.error("Error:", error));
});

Fieldload.addEventListener("keydown", function(event) {
  if (event.key === "Enter" && this.value.trim() !== "") {
      event.preventDefault();
      let inputValue = this.value.trim();
      console.log("Submitted:", inputValue);
  }
});

Useranswer.addEventListener("keydown", function(event) {
  if (event.key === "Enter" && this.value.trim() !== "") {
      event.preventDefault(); 
      let inputValue = this.value.trim();
      console.log("Answer:", inputValue);
  }
});

function getSpanishWord(){
  fetch('https://orange-space-train-x5qjv4w75vqhv47x-8080.app.github.dev/api/spanish')
  .then(response => response.text()) 
  .then(data => {
      console.log("Response:", data)
      let p = document.createElement('p');
      p.textContent = data;
      document.getElementById('WordText').appendChild(p);
  })
  .catch(error => console.error("Error:", error));
}

function getEnglishWord(){
  fetch('https://orange-space-train-x5qjv4w75vqhv47x-8080.app.github.dev/api/english')
  .then(response => response.text()) 
  .then(data => {
      console.log("Response:", data)
      let p = document.createElement('p');
      p.textContent = data;
      document.getElementById('WordText').appendChild(p);
  })
  .catch(error => console.error("Error:", error));
}


fetch('https://orange-space-train-x5qjv4w75vqhv47x-8080.app.github.dev/api/data', {
  method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({ message: "Hello, backend!" })
})
.then(response => response.text())
.then(data => console.log("Response from server:", data))
.catch(error => console.error("Error:", error));