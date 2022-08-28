
//получение индекса элемента
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

var bookApi = Vue.resource('/books{/id}')

// создание книги
Vue.component('book-form', {
    props: ['books', 'bookAttr'],
    //блок для сохранения данных в переменную
    data: function () {
        return {
            id: '',
            name: '',
            authors: '',
            genre: '',
            year: '',
            listOfComments: ''
        }
    },
    //наблюдатель за изменениями переменной
    watch: {
      bookAttr: function(newVal, oldVal) {
          this.id = newVal.id;
          this.name = newVal.name;
          this.genre = newVal.genre;
          this.year = newVal.year;
          this.authors = newVal.authors;
          this.listOfComments = newVal.listOfComments;
      }
    },
    //контейнер для формочки
    template:
    '<div>' +
           '<input type="text" placeholder="book name" v-model="name"/>' +
           '<input type="text" placeholder="genre name" v-model="genre" />' +
           '<input type="text" placeholder="year" v-model="year" />' +
           '<input type="text" placeholder="authors" v-model="authors" />' +
           '<input type="text" placeholder="listOfComments" v-model="listOfComments" />' +
           '<input type="button" value="Save" @click="save"/>' +
           '<input type="button" value="clear" @click="clear"/>' +
        '</div>',

    methods: {
        clear: function () {
                this.name = '';
                this.authors = '';
                this.genre = '';
                this.year = '';
                this.listOfComments = '';
        },
        save: function () {
            var listOfComments = this.listOfComments.toString().split(',');
            var authors = this.authors.toString().split(',');

            var book = { id: this.id, name: this.name, genre: this.genre, year: this.year,
                authors: authors, listOfComments: listOfComments };

            if (this.id) {
                bookApi.update({id: this.id}, book)
                    .then(result => {
                        result.json()
                            .then(data => {
                                var index = getIndex(this.books, data.id);
                                this.books.splice(index, 1, data);
                                this.id = '';
                                this.name = '';
                                this.authors = '';
                                this.genre = '';
                                this.year = '';
                                this.listOfComments = '';
                        })
                })
            } else {
                bookApi.save({}, book)
                    .then(result => result.json()).then(data => {
                        this.books.push(data);
                        this.name = '';
                        this.authors = '';
                        this.genre = '';
                        this.year = '';
                        this.listOfComments = '';
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
        '{{ book.genre.name }} ' +
        '{{ book.year.dateOfPublish }} ' +
        '{{ book.authors }} ' +
        '{{ book.listOfComments }} ' +
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
    props: ['books'],
    data: function () {
      return {
          book: null
      }
    },
    template:
        '<div style="position: relative; width: 1000px;">' +
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
            var authors = [];
            for (let o of book.authors) {
                authors.push(o.name);
            }
            var listOfComments = [];
            for (let c of book.listOfComments) {
                listOfComments.push(c.commentForBook);
            }
            this.book = {
                id: book.id, name: book.name, genre: book.genre.name, year: book.year.dateOfPublish,
                authors: authors, listOfComments: listOfComments
            };
        }
    }
});

var app = new Vue({
    // # - css selector, id
    el: '#app',
    // разметка
    template: '<books-list :books="books" />',
    //обьект ключ-значение, отображает данные
    data: {
        books: []
    }
});

//<!-- Динамическое присвоение значения переменной. -->
// <blog-post v-bind:comment-ids="post.commentIds"></blog-post>