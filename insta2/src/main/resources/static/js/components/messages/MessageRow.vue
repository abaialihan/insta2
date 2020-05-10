<template>
    <v-card>
        <v-card-title>
            <i>({{ message.id }})</i>
            {{ message.text }}
        </v-card-title>
        <v-card-actions>
            <v-btn icon @click="edit">
                <v-icon>{{ createM }}</v-icon>
            </v-btn>
            <v-btn icon @click="del">
                <v-icon>{{ deleteM }}</v-icon>
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import { mdiDelete } from '@mdi/js'
    import { mdiCreate } from '@mdi/js'

    export default {
        props: ['message', 'editMessage', 'deleteMessage', 'messages'],
        methods: {
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.deleteMessage(this.message)
            },
            data() {
                return {
                    deleteM: mdiDelete,
                    createM: mdiCreate
                }
            }
        }
    }
</script>

<style>
</style>