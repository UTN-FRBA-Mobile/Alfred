# Clases-2018c2
Prácticas de Clase - 2° Cuatrimestre 2018

Este repositorio incluye código inicial para realizar tareas durante las clases. Cada branch representa una práctica distinta.

## Práctica Conectividad

Conectar la aplicación con un servicio REST simple.

* Diseñar el modelo para leer el resultado del servicio con este formato:

```json
{
  "tweets": [
      {
          "profilePic": "url de imagen",
          "name": "nombre",
          "certified": true,
          "username": "@username",
          "content": "el tweet",
          "image": "url de imagen. opcional (puede no venir)",
          "commentCount": número,
          "retweetCount": número,
          "likeCount": número
      }
  ]
}
```

* Armar la interfaz para hacer la llamada con RetroFit a la siguiente URL:

    `https://demo0682762.mockable.io/list`

* Realizar la llamada al servicio en el `onStart` de `TweetsFragment`, pasar la respuesta a `TweetsAdapter` y actualizar este último para mostrar la información de cada tweet.

* Mostrar las imágenes (`profilePic` e `image`) utilizando Picasso.
