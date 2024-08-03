# API REFERENCE

## Authentication

`/api/auth`

-[x] POST `/login`: Login an user with body data _{email, password}_
-[x] POST `/logout`: Logout user loged in if exist.
-[x] POST `/register`: Register an user with body data _{email, password}_
-[x] GET `/user`: Get the logged in user.
-[x] POST `/change-password`: Change user password with body data _{email, password}_

Authentication use cookies to store the user logged in.

## User

`/api/user`

-[x] GET: Get all users
-[x] GET `/:id`: Get user by id
-[x] PUT `/:id`: Update (only nacionality, birthday, credit, name)

## UserFriends

`/api/user-friends`

-[x] GET `/:id`: Get all user id's friends
-[x] GET `/:id/some`: Get some user id's friends with parameters

  | Parameter | Description                                         |
  |-----------|-----------------------------------------------------|
  | initial   | value of the initial item to start displaying       |
  | size      | value of the number of items to display             |

-[x] GET `/:id/suggest`: Get list of friends suggestions for user with _id_

  | Parameter | Description                                         | Default |
  |-----------|---------|-----|
  | initial   | value of the initial item to start displaying       | 0       |
  | size      | value of the number of items to display             | 10      |

-[x] POST `/:id/:friend-id`: Add a friend by _friend-id_ to user by _id_
-[x] DELETE `/:id/:friend-id`: Delete a friend by _friend-id_ in user by _id_ friend's list

## Lodging

`/api/lodging`

- POST `?page=currentPage&size=sizeToShow`: Get results of lodging page with body data

  | Parameter | Description                                         |
  |-----------|-----------------------------------------------------|
  | page      | value of the current page of the objects to display |
  | size      | value of the number of items to display             |

body: JSON representing the value of the filters for the lodging search

```typescript
{
    destination: string
    fromdate: Date
    todate: Date
    passengers: number
    minScore: number
}
```

- GET `/:id`: Get lodging by id
- DELETE `/:id`: Delete lodging by id
- POST: Create with body ...
- GET `/user/:id`: Get posts published by user id
- GET `/search/country/:country`: Get countrys by coincidences

## LodgingBooking
- POST: Create with body
```kotlin
data class BookingDTO(
    var id: Long?,
    var userId: Long,
    var lodgingId: Long,
    var fromDate: LocalDate,
    var toDate: LocalDate,
    var totalCost: Float,
    var score: Float?,
    var comment: String?
)
```
- GET `/:id`: Get booking by id
- GET `/user/:id`: Get bookings by user id
- GET `/lodging/:id`: Get bookings by lodging id
- POST `/review`: Score a booking with body
``` kotlin
data class BokkingReviewDTO(
    var bookingId: Long,
    var score: Float,
    var comment: String
)
```