package com.microservice.event.handle;

import java.util.List;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.microservice.client.IsbnClient;
import com.microservice.event.NewBookCreatedEvent;
import com.microservice.exceptions.BadRequestException;
import com.microservice.model.Book;
import com.microservice.service.BookService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

@Log4j2
@Service
@AllArgsConstructor
public class BookCreatedHandler {

	private BookService bookService;

	private final IsbnClient isbnClient;

	@EventListener
	@Async("asyncTaskExecutor")
	public void onBookCreatedEvent(NewBookCreatedEvent event) {
		log.debug("onBookCreatedEvent {}", event);

		Book book = bookService.findById(event.getBookId())
				.orElseThrow(() -> new BadRequestException("Not found book for event"));
		String fetchedBookName = fetchBookNameByISBN(event.getIsbn());
		if (StringUtils.isEmpty(fetchedBookName)) {
			throw new BadRequestException("Not found book onlinet");
		}
		book.setName(fetchedBookName);
		bookService.save(book);
	}

	@SuppressWarnings("unchecked")
	private String fetchBookNameByISBN(String isbn) {
		Map<String, Object> data = isbnClient.fetchBookByISBN("isbn:" + isbn);
		if (data.containsKey("totalItems")) {
			int count = (int) data.get("totalItems");
			if (count == 0) {
				return "";
			}
			List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("items");
			return list.stream().map(item -> {
				Map<String, Object> itemInfo = (Map<String, Object>) item.get("volumeInfo");
				return (String) itemInfo.get("title");
			}).findFirst().orElse("");
		}
		return "";
	}
}
