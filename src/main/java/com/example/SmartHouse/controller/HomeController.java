package com.example.SmartHouse.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.SmartHouse.repository.RoomRepository;

@RestController                     // Этот класс будет обрабатывать HTTP-запросы
@RequestMapping("/api/homes")       // Все методы внутри будут доступны по пути /api/homes
public class HomeController {

    @Autowired                      // Spring сам подставит готовый объект HomeRepository
    private HomeRepository homeRepository;

    @Autowired
    private RoomRepository roomRepository;

    // 1. СОЗДАТЬ НОВЫЙ ДОМ (POST)
    @PostMapping
    public ResponseEntity<Home> createHome(@RequestBody Home home) {
        // @RequestBody означает: взять JSON из тела запроса и превратить в объект Home
        Home savedHome = homeRepository.save(home);
        return new ResponseEntity<>(savedHome, HttpStatus.CREATED);
    }

    // 2. ПОЛУЧИТЬ ВСЕ ДОМА (GET)
    @GetMapping
    public List<Home> getAllHomes() {
        return homeRepository.findAll();
    }

    // 3. ПОЛУЧИТЬ ДОМ ПО ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Home> getHomeById(@PathVariable Long id) {
        // @PathVariable извлекает значение {id} из URL
        Optional<Home> home = homeRepository.findById(id);
        if (home.isPresent()) {
            return ResponseEntity.ok(home.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. ОБНОВИТЬ ДОМ (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Home> updateHome(@PathVariable Long id, @RequestBody Home homeDetails) {
        Optional<Home> optionalHome = homeRepository.findById(id);
        if (optionalHome.isPresent()) {
            Home existingHome = optionalHome.get();
            existingHome.setName(homeDetails.getName());
            existingHome.setAddress(homeDetails.getAddress());
            // другие поля, если есть
            Home updatedHome = homeRepository.save(existingHome);
            return ResponseEntity.ok(updatedHome);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. УДАЛИТЬ ДОМ (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHome(@PathVariable Long id) {
        if (homeRepository.existsById(id)) {
            homeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. ДОБАВИТЬ КОМНАТУ В ДОМ (POST)
    @PostMapping("/{homeId}/rooms")
    public ResponseEntity<Room> addRoomToHome(@PathVariable Long homeId, @RequestBody Room room) {
        Optional<Home> homeOpt = homeRepository.findById(homeId);
        if (homeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Home home = homeOpt.get();
        room.setHome(home);          // связываем комнату с домом
        Room savedRoom = roomRepository.save(room);
        // Также можно добавить комнату в список rooms в доме, но это не обязательно для БД
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    // 7. ПОЛУЧИТЬ ВСЕ КОМНАТЫ ДОМА (GET)
    @GetMapping("/{homeId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHome(@PathVariable Long homeId) {
        Optional<Home> homeOpt = homeRepository.findById(homeId);
        if (homeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Ищем все комнаты, у которых home.id = homeId
        // Для этого нужно добавить метод в RoomRepository (см. ниже)
        List<Room> rooms = roomRepository.findByHomeId(homeId);
        return ResponseEntity.ok(rooms);
    }
}