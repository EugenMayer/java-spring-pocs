# WAT

Different pocs for java spring boot

## Run tests

```bash
# start our mysql db on port 3307
docker-compose up -d 
./gradlew test
```

## POCs
### JPA pocs

Mainly pocs for different Many2Many relation types you might need
especially during mapping legacy applications

#### ManyToMany relations
pocs for building `@ManyToMany` relations

**using PKs** 

Building a `@ManyToMany` relation PKs is the most simple task
in our lineup and it works for:

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/domain/pk)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/repository/ParentPkBasedRepositoryTest.java)

Status
 - [done] creating (Cascade) children through the parent
 - [done] loading a parent including is children
 
 
**using a non-PK key**

When using a non-PK key things getting a lot hard, even if the key is a unique
key on our entity

- [Implementation](https://github.com/EugenMayer/java-spring-pocs/tree/master/src/main/java/de/kontextwork/poc/spring/many2many/domain/naturalid)
- [Test](https://github.com/EugenMayer/java-spring-pocs/blob/master/src/test/java/de/kontextwork/poc/spring/many2many/repository/ParentNaturalIdBasedRepositoryTest.java)

Status
 - [done] creating (Cascade) children through the parent
 - [broken] loading a parent including is children
 
 As you can see, loading the parent will not load up the child relation, see [this issue](https://github.com/EugenMayer/java-spring-pocs/issues/1)
 
