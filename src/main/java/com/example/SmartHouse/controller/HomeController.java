package com.example.SmartHouse.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartHouse.entity.Home;
import com.example.SmartHouse.entity.Room;
import com.example.SmartHouse.repository.HomeRepository;
import com.example.SmartHouse.repository.RoomRepository; // подключение библиотеки для логирования
import com.example.SmartHouse.util.TelegramService;

@RestController                     // Этот класс будет обрабатывать HTTP-запросы
@RequestMapping("/api/homes")       // Все методы внутри будут доступны по пути /api/homes
public class HomeController {
     
    @Autowired                      // Spring сам подставит готовый объект HomeRepository
    private HomeRepository homeRepository;

    @Autowired
    private RoomRepository roomRepository;

    //Внедрение логирования
    @Autowired
    private TelegramService telegramService;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);// включение логирования

    // 1. СОЗДАТЬ НОВЫЙ ДОМ (POST)
    @PostMapping
    public ResponseEntity<Home> createHome(@RequestBody Home home) {
        // @RequestBody означает: взять JSON из тела запроса и превратить в объект Home
        log.info("Запрос на создание дома: {}", home.getName()); //лог на создание дома
        Home savedHome = homeRepository.save(home);
        log.info("Регистрация дома с id: {}", savedHome.getId());
        telegramService.sendMessage("Новый дом создан: " + savedHome.getName()); //Отправка сообщения в ТГ
        return new ResponseEntity<>(savedHome, HttpStatus.CREATED);
    }

    // 2. ПОЛУЧИТЬ ВСЕ ДОМА (GET)
    @GetMapping
    public List<Home> getAllHomes() {
        log.info("Запрос на получение списка всех домов");
        return homeRepository.findAll();
    }

    // 3. ПОЛУЧИТЬ ДОМ ПО ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Home> getHomeById(@PathVariable Long id) {
        log.info("Запрос на получение дома с id: {}", id);
        // @PathVariable извлекает значение {id} из URL
        Optional<Home> home = homeRepository.findById(id);
        if (home.isPresent()) {          
            log.info("Дом с id {} найден", id);
            return ResponseEntity.ok(home.get());
        } else {
            log.warn("Дом с id {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    // 4. ОБНОВИТЬ ДОМ (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Home> updateHome(@PathVariable Long id, @RequestBody Home homeDetails) {
        log.info("Запрос на обновление дома с id: {}", id);
        Optional<Home> optionalHome = homeRepository.findById(id);
        if (optionalHome.isPresent()) {
            Home existingHome = optionalHome.get();
            existingHome.setName(homeDetails.getName());
            existingHome.setAddress(homeDetails.getAddress());
            Home updatedHome = homeRepository.save(existingHome);
            log.info("Дом обновлён: {}", updatedHome.getName());
            telegramService.sendMessage("Дом обновлён: " + updatedHome.getName()); //Отправка сообщения в ТГ
            return ResponseEntity.ok(updatedHome);
        } else {
            log.warn("Дом с id {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    // 5. УДАЛИТЬ ДОМ (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHome(@PathVariable Long id) {
        if (homeRepository.existsById(id)) {
            homeRepository.deleteById(id);
            telegramService.sendMessage("Дом удалён из списка id " + id); //Отправка сообщения в ТГ
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Дом с id {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    // 6. ДОБАВИТЬ КОМНАТУ В ДОМ (POST)
    @PostMapping("/{homeId}/rooms")
    public ResponseEntity<Room> addRoomToHome(@PathVariable Long homeId, @RequestBody Room room) {
        log.info("Запрос на добавление комнаты '{}' в дом с id {}", room.getName(), homeId);
        Optional<Home> homeOpt = homeRepository.findById(homeId);
        if (homeOpt.isEmpty()) {
            log.warn("Дом с id {} не найден, комната не добавлена", homeId);
            return ResponseEntity.notFound().build();
        }
        Home home = homeOpt.get();
        room.setHome(home);          // связываем комнату с домом
        Room savedRoom = roomRepository.save(room);
        log.info("Комната добавлена с id: {} в дом '{}'", savedRoom.getId(), home.getName());
        // Также можно добавить комнату в список rooms в доме, но это не обязательно для БД
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    // 7. ПОЛУЧИТЬ ВСЕ КОМНАТЫ ДОМА (GET)
    @GetMapping("/{homeId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHome(@PathVariable Long homeId) {
        log.info("Запрос на получение комнат дома с id: {}", homeId);
        Optional<Home> homeOpt = homeRepository.findById(homeId);
        if (homeOpt.isEmpty()) {
            log.warn("Дом с id {} не найден", homeId);
            return ResponseEntity.notFound().build();
        }        
        List<Room> rooms = roomRepository.findByHomeId(homeId);
        log.info("Найдено {} комнат в доме id {}", rooms.size(), homeId);
        return ResponseEntity.ok(rooms);
    }
}