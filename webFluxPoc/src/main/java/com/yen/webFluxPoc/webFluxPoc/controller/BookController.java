package com.yen.webFluxPoc.webFluxPoc.controller;

import com.yen.webFluxPoc.webFluxPoc.model.Book;
import com.yen.webFluxPoc.webFluxPoc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("")
    public Flux<Book> all(){
        return this.bookService.findAll();
    }

    @PostMapping("")
    public Mono<Book> create(@RequestBody Book book){
        return this.bookService.save(book);
    }

    @GetMapping("/{id}")
    public Mono<Book> getById(@PathVariable("id") Integer id){
        return this.bookService.findById(id);
    }

//    @PutMapping("/{id}")
//    public Mono<Book> update(@PathVariable("id") Integer id){
//        return this.bookService.up()
//    }

}
