db = db.getSiblingDB('firstDb');

// Keep init minimal and idempotent.
if (db.getCollectionNames().indexOf('healthcheck') === -1) {
  db.createCollection('healthcheck');
  db.healthcheck.insertOne({ status: 'ok', createdAt: new Date() });
}
