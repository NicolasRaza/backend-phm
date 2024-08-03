db.getCollection("lodging").find({
    "clickInfo": {$not: { $size: 0 } }
}).map((hospedaje) => {
    return {
        id: hospedaje._id,
        name: hospedaje.name,
        clicks: hospedaje.clickInfo.length
    }
}).toArray().sort((a, b) => b.clicks - a.clicks).slice(0, 1)