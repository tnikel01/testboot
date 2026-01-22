const btn12 = document.getElementById('btn12'); //english-spanish
const btn21 = document.getElementById('btn21'); //spanish-english
const btnadd = document.getElementById('btnadd');
const addSpan = document.getElementById('addSpan');
const addEn = document.getElementById('addEn');
const Fieldload = document.getElementById('load');
const Useranswer = document.getElementById('Useranswer');
const messageContainer = document.getElementById('learndiv');
const msgbox = document.getElementById('msgbox');
const esText = document.getElementById('e-sText');
const btnskip = document.getElementById('skip');
const btnback = document.getElementById('back');
const btnclose = document.getElementById('close');
let selec = 0;
let id = -1;

btn12.addEventListener('click', function() {
  selec = 1;

  btn12.classList.add("hidden");
  btn21.classList.add("hidden");

  messageContainer.style.display = "block";
  esText.textContent = 'English-Spanish';

  getEnglishWord();
});

btn21.addEventListener('click', function() {
   selec = 2;

  btn12.classList.add("hidden");
  btn21.classList.add("hidden");

  messageContainer.style.display = "block";
  esText.textContent = 'Spanish-English';

  getSpanishWord();
});

btnback.addEventListener('click', function() {
  btn12.classList.remove("hidden");
  btn21.classList.remove("hidden");

  esText.textContent = "";
  messageContainer.style.display = "none";
});

btnclose.addEventListener('click', function() {
  console.log("CLOSE");
  document.getElementById('listDiv').classList.add("hidden");
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
    getWordListFromBackend();
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
        getWordListFromBackend();
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

btnskip.addEventListener('click', function(){
  if(selec == 2){
    getSpanishWord();
  }else{
    getEnglishWord();
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
    }else{
      msgbox.textContent = data.result;
      setTimeout(function() {
        msgbox.textContent = "";
      }, 1000);
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
    }else{
      msgbox.textContent = data.result;
      setTimeout(function() {
        msgbox.textContent = "";
      }, 1000);
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

function getWordListFromBackend() {
    fetch('/api/list')
        .then(response => {
            if (!response.ok) {
                throw new Error("HTTP error " + response.status);
            }
            return response.json();  // Expecting a String Array from backend
        })
        .then(wordArray => {
            // Pass the fetched array to the updateList function
            updateList(wordArray);
        })
        .catch(error => console.error("Error fetching data:", error));
}

function updateList(wordArray) {
    const listElement = document.getElementById('listenName');

    listElement.innerHTML = '';

    if (wordArray.length % 2 !== 0) {
        console.error("The word array must have an even number of elements.");
        return;
    }

    for (let i = 0; i < wordArray.length; i += 2) {
        const englishWord = wordArray[i];
        const spanishWord = wordArray[i + 1];

        const listItem = document.createElement('li');
        listItem.textContent = `${englishWord} - ${spanishWord}`;

        listElement.appendChild(listItem);
    }
    document.getElementById('listDiv').classList.remove("hidden");
}

getWordListFromBackend();