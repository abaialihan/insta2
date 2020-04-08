
var messageApi = Vue.resource('/message{/id}');

// Определяем новый компонент под именем message-row
Vue.component('message-row', {
  props: ['message'],
  template: '<div><i> ({{message.id}}) </i> {{ message.text }} </div>'
});

// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {
  props: ['messages'],
  template:
    '<div>' +
        '<message-row v-for="message in messages" :key="message.id" :message="message" />' +
    '</div>',
   created: function(){
     messageApi.get().then(result =>
        result.json().then(data =>
            data.forEach(message => this.messages.push(message))
        )
     )
   }
});

/*
var app - экземпляр нашего приложения
el - идентификатор html тега
data - обьект который отображает в виде ключ-значение
*/

var app = new Vue({
  el: '#app',
  template: '<messages-list :messages="messages" />',
  data: {
    messages: []
  }
});