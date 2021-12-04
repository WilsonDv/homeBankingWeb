const app = Vue.createApp({

    data() {
        return {
            clientCards: [],
            clientLoans: [],
            cardsT: [],
            cardsColor: [],
            cardsTypeCredit: [],
            cardsTypeDebit: [],
            vencimiento: [],
            todayDate: '',
            hijoCardsClick: false,
            todayDate : new Date().toISOString().slice(0,10),
        }
    },

    created() {
      
        axios.get('/api/clients/current')
        .then(response => {
        this.clientCards = response.data
        this.cardsT = response.data.cards.filter( card=> card.cardDelete == false)
        this.clientLoans = response.data.clientLoans
        this.obtenerTipoDetarjeta(this.cardsT)
        this.cortarFechaVencimiento(this.cardsTypeCredit)
        this.cortarFechaVencimiento(this.cardsTypeDebit)
        this.cortarFechaVigencia(this.cardsTypeCredit)
        this.cortarFechaVigencia(this.cardsTypeDebit)
    })
    .catch(error => {
        return error.message;
    })

    },
    methods: {

        eliminarTarjeta(cvv){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes!'
              }).then((result) => {
                if (result.isConfirmed) {
                  Swal.fire(
                    'Successful!',
                    'Successfully related.',
                    'success'
                  )
                  console.log(cvv)
                  axios.delete(`/api/clients/delete/cards/${cvv}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {

                        setTimeout(location.reload.bind(location), 1500)
                    })
                    .catch(error => {
                        return error.response.data
                    })
                } 
            })
        },
        cerrar() {
            axios.post('/api/logout')
            .then(response => {  

                console.log(response)
                console.log('signed out!!!')
                window.location.replace("/web/index.html")
             })
             .catch(error => {
                return error.message
            })
                
        },
        
         obtenerTipoDetarjeta(arrayTypeCard){
                this.cardsTypeCredit = arrayTypeCard.filter(tipo => tipo.type === "CREDIT")
                this.cardsTypeDebit = arrayTypeCard.filter(tipo => tipo.type === "DEBIT") 
             },

        enConstruccion() {

            Swal.fire({
                icon: 'info',
                title: 'Oops...',
                text: 'We are working on it, Thank you for your understanding!',
              })
        },
        cortarFechaVencimiento(arrayVencimiento) {
            arrayAux = []
            for (let i = 0; i < arrayVencimiento.length; i++) {
                
               const dateNueva =  arrayVencimiento[i].thruDate.split('').slice(0, 10).join('') 
                arrayVencimiento[i].thruDate = dateNueva
                arrayAux.push(arrayVencimiento[i])
           }
           return arrayVencimiento

        },
        cortarFechaVigencia(arrayFechaVigente) {
            arrayAux = []
            for (let i = 0; i < arrayFechaVigente.length; i++) {
                
               const dateNueva =  arrayFechaVigente[i].fromDate.split('').slice(0, 10).join('') 
                arrayFechaVigente[i].fromDate = dateNueva
                arrayAux.push(arrayFechaVigente[i])
           }
           return arrayFechaVigente

        },
        calcularVencimiento(){
            console.log(this.todayDate) 
        },

        cardsClick(){
            console.log(this.hijoCardsClick)
            return this.hijoCardsClick = !this.hijoCardsClick
        },

    }, //llave methods 


})

const verAppVue = app.mount("#app") 