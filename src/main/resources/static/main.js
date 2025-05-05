const btn12 = document.getElementById('btn12');
const btn21 = document.getElementById('btn21');
const btnadd = document.getElementById('btnadd');
const addSpan = document.getElementById('addSpan');
const addEn = document.getElementById('addEn');
const Fieldload = document.getElementById('load');
const Useranswer = document.getElementById('Useranswer');
const messageContainer = document.getElementById('learndiv');
let selec = 0;
let id = -1;

btn12.addEventListener('click', function() {
  const newMessage = document.createElement('p');

  selec = 1;

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

  selec = 2;

  messageContainer.style.display = "block";
  newMessage.textContent = 'Spanish-English';

  messageContainer.appendChild(newMessage);
  getSpanishWord();
});

btnadd.addEventListener('click', function(){
  fetch('/api/addWord', {
    method: "POST",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify({En: addEn.value, Spa: addSpan.value})
  })
  .then(response => response.text())
  .then(data => {console.log("Response from server:", data)
    addEn.value="";
    addSpan.value="";
  })
  .catch(error => console.error("Error:", error));
});

Fieldload.addEventListener("keydown", function(event) {
  if (event.key === "Enter" && this.value.trim() !== "") {
    event.preventDefault();
    let inputValue = this.value.trim();
    console.log("Submitted:", inputValue);

    fetch('/api/changeFile', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ File: inputValue })
    })
    .then(response => response.json())
    .then(data => {
      console.log("Response from server:", data);
      if (data.message) {
        alert(data.message);
      } else if (data.error) {
        alert("Fehler: " + data.error);
      }
      this.value = ""; // Eingabefeld leeren
    })
    .catch(error => {
      console.error("Error:", error);
      alert("Ein Fehler ist aufgetreten");
    });
  }
});

Useranswer.addEventListener("keydown", function(event) {
  if (event.key === "Enter" && this.value.trim() !== "") {
      event.preventDefault(); 
      let inputValue = this.value.trim();
      if(selec == 2){
        checkEnglishWord(inputValue);
      }else{
        checkSpanishWord(inputValue);
      }
      console.log("Answer:", inputValue);
      this.value = "";
  }
});

function checkSpanishWord(answer) {
  fetch('/api/checkSpanish', {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ message: answer, id: id })
  })
  .then(response => response.json())
  .then(data => {console.log("Response from server:", data)
    if (data.result === "correct") { 
      getEnglishWord();
    }
  })
  .catch(error => console.error("Error:", error));
}


function checkEnglishWord(answer){
  fetch('/api/checkEnglish', {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ message: answer, id: id })
  })
  .then(response => response.json())
  .then(data => {console.log("Response from server:", data)
    if (data.result === "correct") { 
      getSpanishWord();
    }
  })
  .catch(error => console.error("Error:", error));
}

function getSpanishWord(){
  fetch('/api/spanish')
  .then(response => response.text()) 
  .then(data => {
      console.log("Response:", data)
      let wordData = data.split(',');
      let word = wordData[0];
      id = parseInt(wordData[1]); 
      let wordTextElement = document.getElementById('WordText');
      wordTextElement.innerHTML = "";
      let p = document.createElement('p');
      p.textContent = word;
      wordTextElement.appendChild(p);
  })
  .catch(error => console.error("Error:", error));
}

function getEnglishWord(){
  fetch('/api/english')
  .then(response => response.text()) 
  .then(data => {
      console.log("Response:", data)
      let wordData = data.split(',');
      let word = wordData[0];
      id = parseInt(wordData[1]); 
      let wordTextElement = document.getElementById('WordText');
      wordTextElement.innerHTML = "";
      let p = document.createElement('p');
      p.textContent = word;
      wordTextElement.appendChild(p);
  })
  .catch(error => console.error("Error:", error));
}

fetch('/api/data', {
  method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({ message: "Hello, backend!" })
})
.then(response => response.text())
.then(data => console.log("Response from server:", data))
.catch(error => console.error("Error:", error));