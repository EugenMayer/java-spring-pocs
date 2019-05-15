[![Build Status](https://travis-ci.org/EugenMayer/java-spring-pocs.svg?branch=master)](https://travis-ci.org/EugenMayer/java-spring-pocs)

# WAT

Different pocs for java spring boot

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
```

We are using an non-memory h2 database in the tests to make it easier to connect and debug the DDL during a test.

## POCs
Different POCs of any kinds

### JPA

Mainly POCs for different relation types you might need.
It tries to also covered needs of legacy databases or at least DDLs which have not been designed for spring/by spring


#### JPA: ManyToMany relation
pocs for building `@ManyToMany` relations. Overview:

 - using PKs only (both PKs)
 - using one PK, one not PK
 - using both not PK
 - using both not PK with Inheritance

**JPA: ManyToMany - using both PKs (parent pk / child pk)** 

Building a `@ManyToMany` relation PKs is the most simple task
in our lineup and it works for:

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/pk/domain)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/pk/repository/ParentPkBasedRepositoryTest.java)

Status
 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including is children
 
 
**JPA: ManyToMany - using a non-PK key (parent pk, child non pk)**

!! It suddenly started to work for all tested database - yet i cannot explain why


~~When using a non-PK key things getting a lot hard, even if the key is a unique
key on our entity~~

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/nonpk/domain/)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/nonpk/repository/ParentNonPkBasedRepositoryTest.java)

Status

 - [done] creating (Cascade) children through the parent
 - ~~[broken] loading a parent including is children~~
 - [done] loading a parent including is children
 
 ~~As you can see, loading the parent will not load up the child relation, see~~
 
**JPA: ManyToMany - using a non-PK key (parent pk, child non pk) with a Service**

!! This implementation is no longer needed since suddenly the non-pk relations can be read by JPA. Yet unexplained.

~~As in `using a non-PK key` we can save those relations using this implementation but we could not read them (children are missing). 
Thus in this strategy we add a service to do that for us, handling the loading using a `native` query.~~ 

~~Hint: Right now it is a `native @Query` since we seem not to be able to create a `JPA INNER JOiN` in the `@joinTable` - for this to work we would rather
remove the `joinTable` relation entirely and create a `association table` - but this would mean a lot more code on writing all the `save`
methods to create the relation - seems to be a bad trade off IMHO~~ 

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/nonpkservice/domain)
- [Test Child Repo](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/nonpkservice/repository/ChildNonPkServiceBasedRepositoryTest.java)
- [Test Parent Service](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/nonpkservice/service/ParentNonPkServiceTest.java)~~

Status

- ~~[done] creating (Cascade) children through the parent~~
- ~~[done] loading a parent including is children~~

**JPA: ManyToMany - using a both non-PK keys (parent non pk, child non pk) with a Service**

!! Also this started to work out of a sudden - yet unexplained. So its just a joinTable as usual, thats it

~~This time, both parent and child are in relation an `non ok key` ( on both sides). We again use a service
for the loading part.~~ 

~~In addition to the other non-pk (child) examples, we now also have to implement `Serializable` on the parent too.~~

~~Anything else is as we have it in `using a non-PK key (parent pk, child non pk) with a Service`~~ 

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/bothnonpkservice/domain)
- [Test Child Repo](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/bothnonpkservice/repository/ChildBothNonPkServiceBasedRepositoryTest.java)
- [Test Parent Service](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/bothnonpkservice/service/ParentBothNonPkServiceTest.java)

Status
 - ~~ [done] creating (Cascade) children through the parent~~
 - ~~ [done] loading a parent including is children~~

**JPA: ManyToMany - using a both non-PK keys (or PKs) with Parent/Child in inheritance with a Service**

~~This is like `using a both non-PK keys` above~~, Actually its no longer the same, since this is really 
 not working as the old have been, but all the other started working out of a sudden. This setup is both no pk, but this time the Parent 
 and Child subtypes using a `discriminator` using a `InheritanceType.SINGLE_TABLE` - this requires us to finally build the whole
 relation ourself - saving and loading - `@joinTable` can no longer be used in this case.

So we added relation and child saving in the service `save` as also a new `@Entity` for the association table
(the actual `@joinTable`)

This case would be the same when using `pk keys` - it's for both cases. If you are allowed / can,
rather use `@MappedSuperclass` and use the classic `@joinTable` implementation with PKs or without PKs
as shown before

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/inheritance/domain)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/inheritance/service/ParentBothNonPkSelfServiceTest.java)

Status
 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including is children