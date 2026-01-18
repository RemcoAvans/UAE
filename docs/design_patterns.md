### Design Patterns UAE Applicatie

Dit document beschrijft de design patterns die zijn toegepast in de UAE (Ultimate Activity Experience) applicatie, specifiek gericht op de functionaliteit rondom het aanmaken van activiteiten.

---

### 1. Repository Pattern

#### Motivatie
Het Repository Pattern wordt gebruikt om de business logica (Use Cases) los te koppelen van de data-toegangslaag (Exposed ORM / Database). 

**Voordelen:**
- **Scheiding van belangen (Separation of Concerns):** De Use Case hoeft niet te weten hoe data precies wordt opgeslagen of opgehaald.
- **Vervangbaarheid:** We kunnen eenvoudig wisselen tussen een database-implementatie (`ActivityRepository`) en bijvoorbeeld een in-memory implementatie voor unit tests, zonder de Use Cases aan te passen.
- **Centrale logica:** Queries en data-mapping (`toActivity`) bevinden zich op één plek.

#### Klassendiagram
```mermaid
classDiagram
    class IActivityRepository {
        <<interface>>
        +create(entity: Activity) Activity
        +getById(id: Int) Activity
        +updatePhotoUrl(id: Int, url: String)
    }
    class ActivityRepository {
        +create(entity: Activity) Activity
        +getById(id: Int) Activity
        +updatePhotoUrl(id: Int, url: String)
        -toActivity(row: ResultRow) Activity
    }
    class CreateActivityUseCase {
        -repository: IActivityRepository
        +execute(input: CreateActivityDto)
    }

    IActivityRepository <|.. ActivityRepository
    CreateActivityUseCase --> IActivityRepository
```

#### Sequence Diagram (Create Activity)
```mermaid
sequenceDiagram
    participant Client
    participant UseCase as CreateActivityUseCase
    participant Repo as IActivityRepository
    participant DB as Database (Exposed)

    Client->>UseCase: execute(dto)
    UseCase->>Repo: create(activity)
    Repo->>DB: ActivityTable.insert()
    DB-->>Repo: New ID
    Repo-->>UseCase: Created Activity
    UseCase-->>Client: ObjectResult(Activity)
```

---

### 2. Strategy Pattern (Polymorfisme in DTOs & Use Cases)

#### Motivatie
Bij het aanmaken van activiteiten hebben we te maken met verschillende types (Food, Sport, Culture) die elk hun eigen specifieke data hebben. In plaats van één grote klasse met veel optionele velden, gebruiken we polymorfisme in de DTO's en een 'Strategy-achtige' aanpak in de Use Case.

**Voordelen:**
- **Type-veiligheid:** Specifieke velden zoals `Cuisine` horen alleen bij `FoodActivity`.
- **Uitbreidbaarheid:** Nieuwe activiteitstypes kunnen worden toegevoegd door een nieuwe DTO-klasse te maken zonder de basislogica te breken.
- **Schonere code:** De Use Case gebruikt een `when`-expressie om de specifieke opslaglogica te bepalen op basis van het type.

#### Klassendiagram
```mermaid
classDiagram
    class CreateActivityDto {
        <<abstract>>
        +title: String
        +description: String
        +type: String
        +toActivity() Activity
    }
    class CreateFoodActivityDto {
        +Cuisine: String
        +PriceRange: String
    }
    class CreateSportActivityDto {
        +difficulty: String
        +isIndoor: Boolean
    }
    
    CreateActivityDto <|-- CreateFoodActivityDto
    CreateActivityDto <|-- CreateSportActivityDto

    class CreateActivityUseCase {
        +execute(input: CreateActivityDto)
    }

    CreateActivityUseCase ..> CreateActivityDto
```

#### Sequence Diagram (Create with Picture & Type handling)
```mermaid
sequenceDiagram
    participant Routes
    participant UC_Pic as CreateActivityWithPictureUseCase
    participant UC_Base as CreateActivityUseCase
    participant Repo as IActivityRepository

    Routes->>UC_Pic: execute(multipartData)
    Note over UC_Pic: Bepaalt type (Food/Sport/etc)
    UC_Pic->>UC_Base: execute(specificDto)
    UC_Base->>Repo: create(BaseActivity)
    UC_Base->>Repo: createFood/Sport/Culture(SpecificData)
    UC_Base-->>UC_Pic: ActivityResult
    
    Note over UC_Pic: Upload foto naar opslag
    UC_Pic->>Repo: updatePhotoUrl(id, url)
    UC_Pic-->>Routes: Success Result
```
