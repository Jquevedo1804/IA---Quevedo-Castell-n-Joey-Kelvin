<div align="center">

# Creación de Dataset y Reconocimiento Facial con CNN

</div>

---

Sistema de reconocimiento facial desarrollado por fases que permite construir un dataset híbrido, entrenar una red neuronal convolucional y reconocer personas conocidas en imágenes o cámara en vivo. El proyecto integra captura de rostros, preprocesamiento, aumento de datos, entrenamiento con transfer learning, calibración de umbral, evaluación y una demo final.

## Equipo

- Rodriguez Rojo Israel Josue - 22170799
- Samano Machado Kevin Jasiel - 22170815
- Quevedo Castellon Joey Kelvin - 22170777

## Arquitectura elegida

El sistema sigue un pipeline por fases. Cada etapa genera una salida que sirve como entrada para la siguiente:

```text
Cámara / imágenes
→ Dataset crudo por persona
→ Detección, recorte y alineación facial
→ Dataset procesado 160x160
→ Aumento de datos
→ División train / val / test
→ Entrenamiento CNN
→ Calibración de umbral
→ Inferencia en imagen o cámara
→ Evaluación final y demo
```

Se eligió esta arquitectura porque permite controlar cada parte del proceso de reconocimiento facial de forma clara y evaluable. Además, facilita repetir fases específicas sin rehacer todo el proyecto, por ejemplo reentrenar el modelo desde los datos ya procesados o recalibrar el umbral después de obtener nuevas métricas.

## Modelos y técnicas utilizados

| Módulo | Técnica / modelo | Tarea |
|---|---|---|
| Captura | OpenCV | Lectura de cámara y guardado de imágenes por persona |
| Detección facial | MTCNN / Haar Cascade | Localización del rostro dentro de cada imagen |
| Preprocesamiento | Recorte, alineación y redimensionamiento | Normalización facial a 160x160 píxeles |
| Aumento de datos | Rotación, brillo, espejo, ruido, zoom, contraste y blur | Generación de variantes para robustecer el dataset |
| Clasificación | ResNet18 o EfficientNet-B0 | Reconocimiento de identidad mediante transfer learning |
| Entrenamiento | PyTorch / Torchvision | Optimización del modelo CNN |
| Calibración | Curvas de precisión, recall y F1 | Selección de umbral para detectar `Desconocido` |
| Evaluación | Matriz de confusión y métricas finales | Validación del rendimiento del sistema |
| Demo | OpenCV | Reconocimiento en imagen estática o cámara en vivo |


## Descripción de cada archivo

**`Face-Recognition Proyect/Scripts/1_Captura.py`** — Fase 1. Captura imágenes desde la cámara web y las guarda en `Dataset/<Persona>/`. Permite indicar el nombre de la persona, cantidad de fotos, tiempo entre capturas e índice de cámara.

**`Face-Recognition Proyect/Scripts/2_Preprocesar.py`** — Fase 2. Detecta el rostro principal de cada imagen, lo alinea, lo recorta y lo redimensiona a 160x160 píxeles. Guarda el resultado en `Dataset_procesado/` y genera logs del proceso.

**`Face-Recognition Proyect/Scripts/3_Aumentar.py`** — Fase 3. Genera imágenes sintéticas a partir del dataset procesado mediante transformaciones como rotación, brillo, espejo, ruido, zoom, contraste, blur y cambios de tono. Guarda el dataset ampliado en `Dataset_aumentado/`.

**`Face-Recognition Proyect/Scripts/4_preparar_clasificacion.py`** — Fase 4. Crea los archivos `train.csv`, `val.csv` y `test.csv` dentro de `splits/`. Mantiene una separación estratificada por clase y evita fuga de datos agrupando las variantes aumentadas con su imagen base.

**`Face-Recognition Proyect/Scripts/5_entrenar_cnn.py`** — Fase 5. Entrena una CNN con transfer learning usando `resnet18` o `efficientnet_b0`. Guarda el mejor modelo como `models/best_model.pth`, además de historial de entrenamiento y métricas de prueba.

**`Face-Recognition Proyect/Scripts/6_inferencia.py`** — Fase 6. Carga el modelo entrenado y realiza predicción sobre una imagen estática. Devuelve la clase predicha, confianza, top 3 de resultados y una imagen anotada.

**`Face-Recognition Proyect/Scripts/7_pipeline_camara.py`** — Fase 7. Ejecuta el reconocimiento facial en cámara en vivo. Detecta rostros, clasifica cada rostro con el modelo entrenado y muestra las etiquetas directamente sobre el video.

**`Face-Recognition Proyect/Scripts/8_umbral.py`** — Fase 8. Evalúa las predicciones del modelo para encontrar un umbral de confianza adecuado. Puede generar una gráfica de precisión, recall y F1, y también guardar el umbral óptimo dentro del checkpoint.

**`Face-Recognition Proyect/Scripts/9_evaluacion.py`** — Fase 9. Consolida los resultados finales del sistema. Genera curvas de entrenamiento, matriz de confusión, reporte JSON y métricas generales del modelo.

**`Face-Recognition Proyect/Scripts/demo.py`** — Punto de entrada final para usar el sistema. Permite ejecutar reconocimiento en cámara o analizar una imagen sin tener que llamar manualmente a cada fase interna.

## Formato del dataset

El dataset se organiza en carpetas, donde cada carpeta representa una clase o persona:

```text
Dataset/
├── Persona_1/
│   ├── imagen_1.jpg
│   ├── imagen_2.jpg
│   └── ...
├── Persona_2/
│   ├── imagen_1.jpg
│   └── ...
└── Persona_N/
    └── ...
```

## Cómo ejecutar

### Probar con una imagen

```powershell
python Scripts/demo.py --imagen "C:\...\foto.jpg" --mostrar
```

### Probar con cámara en vivo

```powershell
python Scripts/demo.py --camara
```