# Spring

## Dependency Injection (DI)
### `@Autowired` is not required 
From Spring 4.3+ constructor injection does not require this annotation. 
```kotlin
@RestController
class PersonController(val personService: PersonService)
```

### Initialization order

1. **Object Instantiation**: Spring creates an instance of your class using the primary constructor. 
At this stage:
    - Any val or var properties initialized in the class body or constructor are evaluated.
    - The `init` block is executed as part of the object construction process.
    - This includes properties initialized using other fields in the class, even if those fields 
are injected by Spring. If the injected field is not yet initialized by Spring, it will be null 
at this point and could cause NPE.
2. **Dependency Injection**: Spring performs dependency injection. This happens after the object 
has been created but before any lifecycle methods like `@PostConstruct` are invoked.
3. **Post-Construct Phase**: If you have methods annotated with @PostConstruct, these will be 
executed after dependency injection is complete. At this point, all injected dependencies should 
be available, and you can safely use them.

**Best Practices**
- Avoid placing logic in init blocks if it relies on Spring-injected dependencies.
- Use @PostConstruct for initialization logic that depends on injected fields.
- Use lazy properties or lateinit var to defer initialization until after dependency injection.