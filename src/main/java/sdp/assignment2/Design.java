package sdp.assignment2;

public final class Design {

    private final String name;
    private final int quantity;
    private final int workUnitsPerItem;

    public Design(String name, int quantity, int workUnitsPerItem) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Design name must not be empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (workUnitsPerItem <= 0) {
            throw new IllegalArgumentException("Work units must be greater than zero.");
        }

        this.name = name.trim();
        this.quantity = quantity;
        this.workUnitsPerItem = workUnitsPerItem;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getWorkUnitsPerItem() {
        return workUnitsPerItem;
    }

    public long getTotalWorkUnits() {
        return (long) quantity * workUnitsPerItem;
    }
}