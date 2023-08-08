package com.tenagrim.telegram.service;

import com.tenagrim.telegram.model.Question;
import com.tenagrim.telegram.model.Session;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.repository.SessionRepository;
import com.tenagrim.telegram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByNickname(username);
    }

    public void updateSession(User user) {
        Session session = new Session();
        session.setCreateDate(new Date());
        session.setUser(user);
        Session savedSession = sessionRepository.save(session);
        user.setActiveSession(savedSession);
    }

    public void updateLastQuestion(User user, Question question) {
        Session session = user.getActiveSession();
        // todo if null
        session.setLastQuestion(question);
        sessionRepository.save(session);
    }

    public void finishSession(User user, Date finishDate){
        Session activeSession = user.getActiveSession();
        activeSession.setFinishDate(finishDate);
        sessionRepository.save(activeSession);
    }


}
