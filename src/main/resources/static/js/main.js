
//получение индекса элемента
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

var bookApi = Vue.resource('/book{/id}')

// создание кники
Vue.component('book-form', {
    props: ['books', 'bookAttr'],
    //блок для сохранения данных в переменную
    data: function () {
        return {
            text: '',
            id: ''
        }
    },
    //наблюдатель за изменениями переменной
    watch: {
      bookAttr: function(newVal, oldVal) {
           this.text = newVal.text;
           this.id = newVal.id;
      }
    },
    //контейнер для формочки
    template:
    '<div>' +
           '<input type="text" placeholder="book name" v-model="text" />' +
           '<input type="button" value="save" @click="save"/>' +
        '</div>',
    //реализаця метода save из 16 строки
    methods: {
        save: function () {
            var book = { text: this.text };

            if (this.id) {
                bookApi.update({id: this.id}, book).then(result => {
                    result.json().then(data => {
                        var index = getIndex(this.books, data.id);
                        this.books.splice(index, 1, data);
                        this.text = ''
                        this.id = ''
                    })
                })
            } else {
                bookApi.save({}, book).then(result =>
                    result.json()).then(data => {
                    this.books.push(data);
                    // очищение поля после ввода
                    this.text = ''

                })
            }
        }
    }
});

// отображение книги
Vue.component('book-row', {
    props: ['book', 'editMethod', 'books'],
    template: '<div><i>(' +
        '{{ book.id }})</i> ' +
        '{{ book.name }} ' +
        '{{book.genre.name}} ' +
        '<span style="position: absolute; right: 0">' +
            '<input type="button" value="edit" @click="edit" />' +
            '<input type="button" value="X" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.book);
        },
        del: function () {
            bookApi.remove({id: this.book.id}).then(result => {
                if (result.ok) {
                    this.books.splice(this.books.indexOf(this.book), 1)
                }
            })
        }
    }
});

Vue.component('books-list', {
    // параметры, которые мы ожидаем
    props: ['books'],
    data: function () {
      return {
          book: null
      }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<book-form :books="books" :bookAttr="book"/>' +
        '<book-row v-for="book in books" :key="book.id" :book="book" ' +
        ':editMethod="editMethod" :books="books"/>' +
        '</div>',
    // инициализация коллекции данными, когда отображается список книг
    created: function () {
        bookApi.get().then(result =>
            result.json().then(data =>
                data.forEach(book => this.books.push(book))
            )
        )
    },
    methods: {
        editMethod: function (book) {
            this.book = book;
        }
    }
});

// создаем экземпляр приложения
var app = new Vue({
    // #- css selector, id
    el: '#app',
    // разметка
    template: '<books-list :books="books" />',
    //обьект ключ-значение, отображает данные
    data: {
        books: []
    }
});