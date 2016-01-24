class CreateTransactions < ActiveRecord::Migration[5.0]
  def change
    create_table :transactions do |t|
      t.string :name
      t.date :date
      t.string :transaction_type

      t.timestamps
    end
  end
end
