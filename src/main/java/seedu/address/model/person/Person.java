package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Optional<Birthday> birthday;
    private final Optional<Relationship> relationship;
    private final Optional<Nickname> nickname;
    private final Optional<Notes> notes;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Optional<Birthday> birthday,
                  Optional<Relationship> relationship, Optional<Nickname> nickname, Optional<Notes> notes,
                  Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.birthday = birthday != null ? birthday : Optional.empty();
        this.relationship = relationship != null ? relationship : Optional.empty();
        this.nickname = nickname;
        this.notes = notes;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Optional<Birthday> getBirthday() {
        return birthday;
    }
    public String getBirthdayValue() {
        return birthday.map(Object::toString).orElse("");
    }
    public Optional<Relationship> getRelationship() {
        return relationship;
    }
    public String getRelationshipValue() {
        return relationship.map(Object::toString).orElse("");
    }
    public Optional<Nickname> getNickname() {
        return nickname;
    }
    public String getNicknameValue() {
        return nickname.map(Object::toString).orElse("");
    }
    public Optional<Notes> getNotes() {
        return notes;
    }
    public String getNotesValue() {
        return notes.map(Object::toString).orElse("");
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && birthday.equals(otherPerson.birthday)
                && relationship.equals(otherPerson.relationship)
                && nickname.equals(otherPerson.nickname)
                && notes.equals(otherPerson.notes)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, birthday, relationship,
                            nickname, notes, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("birthday", birthday)
                .add("relationship", relationship)
                .add("nickname", nickname)
                .add("notes", notes)
                .add("tags", tags)
                .toString();
    }
}
