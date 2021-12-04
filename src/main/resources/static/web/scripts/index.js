
const app = Vue.createApp({

    data() {
        return { 
            email: '',
            password: '',   
            active: false,
            registration: {
                firstName:'',
                lastName: '',
                email:'',
                password: '',
                accountType : '',
            },
            checkOk: false,
            checkError: false            
        }
    },
    methods: {
        
        verInput(){

            let prueba = this.email
            clientAdmin =  prueba.includes("@admin.com")
            
            if(clientAdmin){
                axios.post('/api/login',`email=${this.email}&password=${this.password}`)
                .then(response => {

                    Swal.fire('success')
                    this.checkOK = !this.checkOK
                    window.location.replace("/manager.html")
                })
                .catch(error => {
                this.checkError = !this.checkError  
                Swal.fire({
                    icon: 'error',
                    title: 'Oops... Password or email invalid try again!',
                })
                setTimeout(location.reload.bind(location), 4000)                     
                })

            } else{
                axios.post('/api/login',`email=${this.email}&password=${this.password}`)
                .then(response => {

                    Swal.fire('success')
                    this.checkOK = !this.checkOK

                    window.location.replace("/web/accounts.html")
                })
                .catch(error => {
                this.checkError = !this.checkError  
                Swal.fire({
                    icon: 'error',
                    title: 'Oops... Password or email invalid try again!',
                })
                    setTimeout(location.reload.bind(location), 4000)                     
                })

            }
            
        },

      enviarCamposForm(){
            if(this.registration.firstName !== '' &&  this.registration.lastName !== '' && this.registration.email !== '' && this.registration.password !== '' && this.registration.accountType && this.registration.accountType !== ''){
                let expresionRegular=/^([\da-z_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/
                if (expresionRegular.exec(this.registration.email)){

                    axios.post('/api/clients',`firstName=${this.registration.firstName}&lastName=${this.registration.lastName}&email=${this.registration.email}&password=${this.registration.password}&accountType=${this.registration.accountType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {

                        axios.post('/api/login',`email=${this.registration.email}&password=${this.registration.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                            .then(response => {
                                console.log(response)  
                                window.location.replace("/web/accounts.html")
                            })
                            .catch(error => {
    
                                return error.message
                            })
                    })
                    .catch(error => {
                        
                        return error.message
                    })
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops... Email incorrect',
                      })
                    setTimeout(location.reload.bind(location), 2500)
                 }

            } else {
                this.checkError = !this.checkError
                Swal.fire({
                    icon: 'error',
                    title: 'Oops... Enter data correctly',
                  })
                setTimeout(location.reload.bind(location), 3000)
            }
        },
        formClassActive() {
            return this.active = !this.active
        }

    },//llave methods 
})

app.mount("#app") 
