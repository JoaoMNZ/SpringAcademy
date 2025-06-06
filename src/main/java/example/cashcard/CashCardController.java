package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;
    private CashCardController(CashCardRepository cashCardRepository){
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedID}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedID, Principal principal){
        Optional<CashCard> cashCardOptional = findCashCard(requestedID, principal);
        if(cashCardOptional.isPresent()){
            return ResponseEntity.ok(cashCardOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal){
        CashCard cashCardWithOwner = cashCardRepository.save(new CashCard(null,newCashCardRequest.amount(), principal.getName()));
        URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(cashCardWithOwner.id()).toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedID}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedID, @RequestBody CashCard cashCardUpdate, Principal principal){
        Optional<CashCard> cashCard = findCashCard(requestedID, principal);
        if(cashCard.isPresent()){
            cashCardRepository.save(new CashCard(requestedID, cashCardUpdate.amount(), principal.getName()));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{requestedID}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable Long requestedID, Principal principal){
        Optional<CashCard> cashCard = findCashCard(requestedID, principal);
        if(cashCard.isPresent()){
            cashCardRepository.deleteById(requestedID);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Optional<CashCard> findCashCard(Long requestedId, Principal principal) {
        return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
    }
}
