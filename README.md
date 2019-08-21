[![Build Status](https://travis-ci.org/EugenMayer/java-spring-pocs.svg?branch=master)](https://travis-ci.org/EugenMayer/java-spring-pocs)

# WAT

Different POCs for java spring boot

## Run integration tests

You can use them to analyze the DDL, see if the integration round-robbing work
or how the fields look like.

```bash
# start our mysql db on port 3307
docker-compose up -d 
# test against h2
./gradlew h2Test

# test against mariadb
./gradlew mariadbTest
# or export SPRING_PROFILE_ACTIVE=mariadb + ./gradlew test

# test against mysql
./gradlew mysqlTest
# or export SPRING_PROFILE_ACTIVE=mysql + ./gradlew test

# test against postgres
./gradlew postgresTest
# or export SPRING_PROFILE_ACTIVE=postgres + ./gradlew test

```

We are using an non-memory h2 database in the tests to make it easier to connect and debug the DDL during a test.

## POCs
Different POCs of any kinds

### JPA

Mainly POCs for different relation types you might need.
It tries to also covered needs of legacy databases or at least DDLs which have not been designed for spring/by spring


#### JPA: ManyToMany relation
pocs for building `@ManyToMany` relations. Overview:

 - [works] using PKs only (both PKs) 
 - [works] using one PK, one not PK
 - [works] using both not PK
 - [works*] using both not PK with Inheritance (*fields are moved)
 - [tba] using both PK with Inheritance 
 
##### JPA: ManyToMany - using both PKs (parent pk / child pk) 

Building a `@ManyToMany` relation PKs is the most simple task
in our lineup and it works for:

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/pk)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/pk/repository/ParentPkBasedRepositoryTest.java)

Status
 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including is children
 
 
##### JPA: ManyToMany - using a non-PK key (parent pk, child non pk)

Works straight forward

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/nonpk/)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/nonpk/repository/ParentNonPkBasedRepositoryTest.java)

Status

 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including its children
 
##### JPA: ManyToMany - using a both non-PK keys (parent non pk, child non pk)

Works straight forward

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/bothnonpk)
- [Test Child Repo](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/bothnonpk/repository/ChildBothNonPkServiceBasedRepositoryTest.java)

Status
 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including its children

##### JPA: ManyToMany - using a both non-PK keys (or PKs) with Parent/Child in inheritance

This setup is both no pk, but this time the Parent  and Child subtypes using a `discriminator` using a `InheritanceType.SINGLE_TABLE` - 
In this case, the important hint is, where to put the non-primary join key we have in common on our subtypes.

Even though all subtypes (Child/Parent) have the field `machine` we are not allowed to define that as 
field/column on the `BaseType` Entity - we have to move (and duplicate) that field into each subtype.

This let's us create a `joinTable` relation which would otherwise not be possible since we get a 
"no mapped field for type"


- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/inheritance)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/inheritance/repository/)

Status
 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including its children

##### JPA: ManyToMany - using a composite id and and derived keys

In this setup, we have a more complex relation, which requires us to use a composite key since we want to relate to a
specific combination. This means, a user cannot just be a member of a role - a user is member of a role in a specific space.
The latter is the reason we need a composite key using `EmbeddedId` and also using the derived keys mapped by `MapsId`

A lot of different examples for derived keys can be found [here](https://jeddict.github.io/page.html?l=tutorial/DerivedIdentities#E1B)


- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/naturalassociation)
- [Test](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/test/java/de/kontextwork/poc/spring/many2many/naturalassociation)

