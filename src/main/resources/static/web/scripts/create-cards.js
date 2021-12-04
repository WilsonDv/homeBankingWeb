
const app = Vue.createApp({

    data() {
        return { 
            clientCards: [],
            clientLoans: [],
            clientAccounts: [],
            type: "",
            color: '', 
            account: '', 
            cards: [],        
        }
    },
    created() {

        axios.get('/api/clients/current')
        .then(response => {
        this.clientCards = response.data
        this.cards = response.data.cards
        this.clientLoans = response.data.clientLoans
        this.clientAccounts = response.data.accounts
    })
    .catch(error => {
        return error.message
    })

    },

    methods: {
      
      EnviarCamposCards(){
                
        if(this.type!= '' &&  this.color != '' && this.account != '' ){
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
                    axios.post('/api/clients/current/cards',`type=${this.type}&color=${this.color}&accountNumber=${this.account}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        window.location.replace("/web/cards.html")
                    })
                    .catch(error => {
                    Swal.fire({
                            icon: 'error',
                            title: `Oops... ${error.response.data}`,
                        })
                        setTimeout(location.reload.bind(location), 3000)
                    })
                } else {
                    setTimeout(location.reload.bind(location), 500)
                }
            })
                
        }else {
            Swal.fire({
                icon: 'error',
                title: 'Oops... Please complete all fields',
              })
              setTimeout(location.reload.bind(location), 2500)
        }
        },

        cerrar() {
            axios.post('/api/logout')
            .then(response => {  
                window.location.replace("/web/index.html")
             })
             .catch(error => {
                return error.message
            })
                
        },
        enConstruccion() {

            Swal.fire({
                icon: 'info',
                title: 'Oops...',
                text: 'We are working on it, Thank you for your understanding!',
              })
        },
        
        
    },//llave methods 
})

app.mount("#app") 
