# model
This namespace contains all models.

If you need access to a `Context` from the model, access the global context through `App.getContext()`.

> [!IMPORTANT]  
> All classes of the model (like `ShRoom` or `InformationTitle`) need to implement the `Serializable` interface. Otherwise the app will crash!

# HTML-elements
*Defining the requirements elements in the HTML must fulfill so that the program can find them.*

## Div-Container which contains rooms
Every room must be in a div-container which is located in the body of the html. 
The container must have the class "flex-container".


