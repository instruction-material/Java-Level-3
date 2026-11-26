class Main {
    static class Room {
        private String name;

        Room(String name) { rename(name); }

        void rename(String name) {
            if (name.isBlank()) throw new IllegalArgumentException("Empty room name");
            this.name = name.strip();
        }

        record Code(int value) {
            Code {
                if (value <= 0) throw new IllegalArgumentException("Desk number must be positive");
            }
        }

        class Desk {
            private final Code code;

            Desk(Code code) { this.code = code; }

            String label() {
                // TODO: Read the current owner name and desk number.
                throw new UnsupportedOperationException("Read the current owner name and desk number");
            }
        }
    }

    public static void main(String[] args) {
        var code = new Room.Code(3);
        var room = new Room("North");
        Room.Desk desk = room.new Desk(code);
        System.out.println(desk.label());
        room.rename("Maple");
        System.out.println(desk.label());
    }
}
